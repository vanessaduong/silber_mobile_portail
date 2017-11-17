package esipe.fisa.silber.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fisa.silber.R;

import java.io.IOException;
import java.util.List;
/Users/zouhairhajji/Documents/netbeansprojects/ing3-sem1/android/silber_mobile_portail/app/src/main/java/esipe/fisa/silber/activities/LoginActivity.java
import esipe.fisa.silber.beans.BankStatement;
import esipe.fisa.silber.listeners.OnNavigationItemSelectedListener;
import esipe.fisa.silber.restInterfaces.RetBankStatement;
import esipe.fisa.silber.utils.APIClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListBankStatementActivity extends AppCompatActivity {

    public static final String TAG = "ListBankStatementAct...";
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private RetBankStatement bankStatement;
    private List<BankStatement> bankStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bank_statement);
        OnNavigationItemSelectedListener.setContext(this);
        this.mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final ListView mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                BankStatement bs = bankStatements.get(position);
                Call<ResponseBody> url = bankStatement.getPDFFormat(4);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url.request().url().toString());
                downloadManager.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Bank Statement")
                        .setDescription(bs.toString()));
            }
        });

        this.bankStatement = APIClient.getClient().create(RetBankStatement.class);
        bankStatement.getAllBankStatements().enqueue(new Callback<List<BankStatement>>() {
            @Override
            public void onResponse(Call<List<BankStatement>> call, Response<List<BankStatement>> response) {
                bankStatements = response.body();
                final ArrayAdapter<BankStatement> adapter = new ArrayAdapter<BankStatement>(ListBankStatementActivity.this, android.R.layout.simple_list_item_1, bankStatements);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<BankStatement>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Cannot get data from server", Toast.LENGTH_LONG);
            }
        });
        Log.d(TAG, "onCreate: ffe");


    }


}
