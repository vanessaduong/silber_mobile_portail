package esipe.fisa.silber.utils;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
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

    // Error code
    private static final byte[] MY_ERROR = {0x6F, 0x00};

    // OK Confirmation
    private static final byte[] OK_CONFIRMATION = {0x00, (byte) 0xA4, 0x00, (byte) 0x0C, 0x02, (byte) 0xE1, 0x03};

    // NOK Confirmation
    private static final byte[] NOK_CONFIRMATION = {0x00, (byte) 0xB0, 0x00, 0x00, (byte) 0x0F};


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private byte[] getUrlBytes() {
        try {
            return TunnelSettings.getUrl(this).getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e); // never happens
        }
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        if (Arrays.equals(SELECT_AID_COMMAND, commandApdu))
        {
            Log.d(TAG, "Received and return: " + commandApdu);
            return MY_UID;
        } else if (Arrays.equals(OK_CONFIRMATION, commandApdu)) {
            notifyPaymentAccepted();
            return MY_UID;
        } else if (Arrays.equals(NOK_CONFIRMATION, commandApdu)) {
            notifyPaymentRefused();
            return MY_UID;
        }


        else return MY_ERROR;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "Link deactivated: " + reason);

        notifyLinkDeactivated(reason);
    }

    private void notifyPaymentAccepted() {
        Intent intent = new Intent(BROADCAST_INTENT_PAYMENT_OK);
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
