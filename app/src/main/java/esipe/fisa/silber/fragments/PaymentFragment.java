package esipe.fisa.silber.fragments;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fisa.silber.R;

import java.io.UnsupportedEncodingException;
import java.util.List;

import esipe.fisa.silber.beans.BankStatement;
import esipe.fisa.silber.listeners.OnNavigationItemSelectedListener;
import esipe.fisa.silber.restInterfaces.RetBankStatement;
import esipe.fisa.silber.utils.APIClient;
import esipe.fisa.silber.utils.TunnelApduService;
import esipe.fisa.silber.utils.TunnelSettings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "PaymentFragment";
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private RetBankStatement bankStatement;
    private TextView paymentTitle, paymentConfirmation;
    private LocalBroadcastManager lbm;
    private boolean dataReceived;
    private long requestStartTimeMillis;


    public PaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankStatementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d(TAG, "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");

        final View v = inflater.inflate(R.layout.fragment_payment, container, false);
        this.mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener();
        paymentTitle = (TextView) v.findViewById(R.id.paymentTitle);
        paymentConfirmation = (TextView) v.findViewById(R.id.paymentConfirmation);
        lbm = LocalBroadcastManager.getInstance(this.getContext());

        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mOnNavigationItemSelectedListener != null) {
            //mListener.onFragmentInteraction(uri);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkForNFC();

        lbm.registerReceiver(paymentAcceptedConfirmation, new IntentFilter(TunnelApduService.BROADCAST_INTENT_PAYMENT_OK));
        lbm.registerReceiver(paymentRefusedConfirmation, new IntentFilter(TunnelApduService.BROADCAST_INTENT_PAYMENT_NOK));
        lbm.registerReceiver(linkDeactivatedReceiver, new IntentFilter(TunnelApduService.BROADCAST_INTENT_LINK_DEACTIVATED));
    }

    @Override
    public void onPause() {
        lbm.unregisterReceiver(paymentAcceptedConfirmation);
        lbm.unregisterReceiver(paymentRefusedConfirmation);
        lbm.unregisterReceiver(linkDeactivatedReceiver);

        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mOnNavigationItemSelectedListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void checkForNFC() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this.getActivity().getApplicationContext());
        if (!nfcAdapter.isEnabled()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    private BroadcastReceiver paymentAcceptedConfirmation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getStringExtra("value");
            paymentConfirmation.setText("Payment " + value + " accepted!");
            paymentConfirmation.setVisibility(View.VISIBLE);

        }
    };

    private BroadcastReceiver paymentRefusedConfirmation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            paymentConfirmation.setText("Payment refused!");
            paymentConfirmation.setVisibility(View.VISIBLE);

        }
    };

    private BroadcastReceiver linkDeactivatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //paymentConfirmation.setText("Payment error.");
        }
    };
}
