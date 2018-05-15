package esipe.fisa.silber.utils;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class TunnelApduService extends HostApduService {

    public static final String BROADCAST_INTENT_DATA_RECEIVED = "DATA_RECEIVED";
    public static final String BROADCAST_INTENT_REQUEST_SENT = "REQUEST_SENT";
    public static final String BROADCAST_INTENT_LINK_DEACTIVATED = "LINK_DEACTIVATED";

    private static final String TAG = "PAYMENT_LOG";

    // the response sent from the phone if it does not understand an APDU
    private static final byte[] UNKNOWN_COMMAND_RESPONSE = {(byte) 0xff};

    // the SELECT AID APDU issued by the terminal
    // our AID is 0xF0ABCDEF0000
    private static final byte[] SELECT_AID_COMMAND = {
            (byte) 0x00, // Class
            (byte) 0xA4, // Instruction
            (byte) 0x04, // Parameter 1
            (byte) 0x00, // Parameter 2
            (byte) 0x06, // length
            (byte) 0xF0,
            (byte) 0xAB,
            (byte) 0xCD,
            (byte) 0xEF,
            (byte) 0x00,
            (byte) 0x00
    };

    // OK status sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_RESPONSE_OK = {(byte) 0x90, (byte) 0x00};

    // Custom protocol commands issued by terminal
    private static final byte READ_URL_COMMAND = (byte) 0x01;

    // Custom protocol responses by phone
    private static final byte READ_URL_RESPONSE = (byte) 0x00;

    private boolean isProcessing;

    private int nextSeqNo;
    private ByteArrayOutputStream buffer;
    private String url;

    @Override
    public void onCreate() {
        super.onCreate();

        isProcessing = false;
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

        if (!isProcessing) {
            isProcessing = true;
            buffer = new ByteArrayOutputStream(262144); // 256 KB buffer
            nextSeqNo = 0;
        }

        if (commandApdu[0] == READ_URL_COMMAND) {
            byte[] urlToDownload = getUrlBytes();
            byte[] responseApdu = new byte[urlToDownload.length + 1];
            responseApdu[0] = READ_URL_RESPONSE;
            System.arraycopy(urlToDownload, 0, responseApdu, 1, urlToDownload.length);
            notifyRequestSent();
            return responseApdu;
        } else {
            Log.e(TAG, "Unknown command");
            return UNKNOWN_COMMAND_RESPONSE;
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "Link deactivated: " + reason);

        isProcessing = false;
        notifyLinkDeactivated(reason);
    }


    private void notifyRequestSent() {
        Intent intent = new Intent(BROADCAST_INTENT_REQUEST_SENT);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void notifyLinkDeactivated(int reason) {
        Intent intent = new Intent(BROADCAST_INTENT_LINK_DEACTIVATED);
        intent.putExtra("reason", reason);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

}
