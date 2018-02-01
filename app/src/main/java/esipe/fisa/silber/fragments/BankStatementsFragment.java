package esipe.fisa.silber.fragments;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.fisa.silber.R;

import java.util.List;

import esipe.fisa.silber.beans.BankStatement;
import esipe.fisa.silber.listeners.OnNavigationItemSelectedListener;
import esipe.fisa.silber.restInterfaces.RetBankStatement;
import esipe.fisa.silber.utils.APIClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankStatementsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BankStatementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankStatementsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "LBankStateFragment";
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private RetBankStatement bankStatement;
    private List<BankStatement> bankStatements;
    private ListView mListView;
    private TextView emptyTextView, titleTextView;
    public BankStatementsFragment() {
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
    public static BankStatementsFragment newInstance(String param1, String param2) {
        BankStatementsFragment fragment = new BankStatementsFragment();
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

        //OnNavigationItemSelectedListener.setContext(getActivity().contex);
        //BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.d(TAG, "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");

        final View v = inflater.inflate(R.layout.fragment_bank_statements, container, false);
        this.mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener();
        mListView = (ListView) v.findViewById(R.id.myBankStatementList);
        emptyTextView = (TextView) v.findViewById(R.id.list_empty);
        titleTextView = (TextView) v.findViewById(R.id.myBankStatementTitle);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                PopupMenu popup = new PopupMenu(container.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.document_item_menu,
                        popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        BankStatement bs = bankStatements.get(i);
                        switch (item.getItemId()) {
                            case R.id.viewDoc:

                                Toast.makeText(container.getContext(), " View doc cb licked at position " + " : " + i, Toast.LENGTH_LONG).show();
                                Call<ResponseBody> view_url = bankStatement.getPDFFormat(bs.getIdBankStatement());
                                Uri view_uri = Uri.parse(view_url.request().url().toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(view_uri, "application/pdf");
                                startActivity(intent);
                                break;
                            case R.id.download:
                                Call<ResponseBody> url = bankStatement.getPDFFormat(bs.getIdBankStatement());
                                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url.request().url().toString());
                                downloadManager.enqueue(new DownloadManager.Request(uri)
                                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false)
                                        .setTitle("Bank Statement")
                                        .setDescription(bs.toString()));
                                Toast.makeText(container.getContext(), " Download clicked at position " + " : " + i, Toast.LENGTH_LONG).show();

                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });

            }
        });
        /**mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                BankStatement bs = bankStatements.get(position);
                Call<ResponseBody> url = bankStatement.getPDFFormat(4);
                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url.request().url().toString());
                downloadManager.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Bank Statement")
                        .setDescription(bs.toString()));
            }
        });**/

        this.bankStatement = APIClient.getClient().create(RetBankStatement.class);
        bankStatement.getAllBankStatements().enqueue(new Callback<List<BankStatement>>() {
            @Override
            public void onResponse(Call<List<BankStatement>> call, Response<List<BankStatement>> response) {
                if(response.isSuccessful()){
                    bankStatements = response.body();
                    ArrayAdapter<BankStatement> adapter = new ArrayAdapter<BankStatement>(container.getContext(), android.R.layout.simple_expandable_list_item_1, bankStatements);
                    if (adapter.getCount() == 0){
                        mListView.setVisibility(View.INVISIBLE);
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                    else{
                        mListView.setAdapter(adapter);
                        mListView.setVisibility(View.VISIBLE);
                        emptyTextView.setVisibility(View.INVISIBLE);
                    }
                }
                else
                    emptyTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<BankStatement>> call, Throwable t) {
                Toast.makeText(container.getContext(), "Cannot get data from server", Toast.LENGTH_LONG);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mOnNavigationItemSelectedListener != null) {
            //mListener.onFragmentInteraction(uri);

        }
    }

    /**@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnNavigationItemSelectedListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }**/

    @Override
    public void onDetach() {
        super.onDetach();
        mOnNavigationItemSelectedListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
