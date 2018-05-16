package esipe.fisa.silber.utils;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class TunnelApduService extends HostApduService {

    public static final String BROADCAST_INTENT_PAYMENT_OK = "PAYMENT_ACCEPTED";
    public static final String BROADCAST_INTENT_PAYMENT_NOK = "PAYMENT_REFUSED";
    public static final String BROADCAST_INTENT_LINK_DEACTIVATED = "LINK_DEACTIVATED";

    private static final String TAG = "PAYMENT_LOG";

    // the SELECT AID APDU issued by the terminal
    private static final byte[] SELECT_AID_COMMAND = {
            (byte) 0x00, // Class
            (byte) 0xA4, // Instruction
            (byte) 0x04, // Parameter 1
            (byte) 0x00, // Parameter 2
            (byte) 0x07, // length
            (byte) 0xD2,
            (byte) 0x76,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x85,
            (byte) 0x01,
            (byte) 0x00
    };

    // OK status sent in response to SELECT AID command
    private static final byte[] MY_UID = {(byte) 0x90, (byte) 0x00};

    //private static final byte[] MY_ACCOUNT = {(byte) 0x01, (byte) 0x62, (byte) 0x69, (byte) 0X70};

    // Error code
    private static final byte[] MY_ERROR = {0x6F, 0x00};

    // OK Confirmation
    private static final byte RECEIVE_VALUE = (byte)0x01;

    // NOK Confirmation
    private static final byte[] NOK_CONFIRMATION = {0x02};


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private byte[] getAccountBytes() {
        try {
            byte[] header = {(byte) 0x01};
            byte[] acc = TunnelSettings.getAccount(this).getBytes("ASCII");
            byte[] accToSend = new byte[header.length + acc.length];
            System.arraycopy(header, 0, accToSend, 0, header.length);
            System.arraycopy(acc, 0, accToSend, header.length, acc.length);
            return accToSend;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e); // never happens
        }
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        if (Arrays.equals(SELECT_AID_COMMAND, commandApdu))
        {
            Log.d(TAG, "Application selected");
            return MY_UID;
        } else if (commandApdu[0] == RECEIVE_VALUE) {
            byte[] value = new byte[commandApdu.length-1];
            System.arraycopy(commandApdu,1,value,0,commandApdu.length-1);
            String s = "";
            try {
                s = new String(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Float f = Float.parseFloat(s);
            if (f <= 100 && f>0){
                notifyPaymentAccepted(s);
                byte[] MY_ACCOUNT = getAccountBytes();
                return MY_ACCOUNT;
            }
            else{
                notifyPaymentRefused();
                return NOK_CONFIRMATION;
            }
        }


        else return MY_ERROR;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "Link deactivated: " + reason);

        notifyLinkDeactivated(reason);
    }

    private void notifyPaymentAccepted(String value) {
        Intent intent = new Intent(BROADCAST_INTENT_PAYMENT_OK);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void notifyPaymentRefused() {
        Intent intent = new Intent(BROADCAST_INTENT_PAYMENT_NOK);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void notifyLinkDeactivated(int reason) {
        Intent intent = new Intent(BROADCAST_INTENT_LINK_DEACTIVATED);
        intent.putExtra("reason", reason);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

}
