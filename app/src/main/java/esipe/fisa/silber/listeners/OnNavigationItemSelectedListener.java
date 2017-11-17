package esipe.fisa.silber.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.fisa.silber.R;

import esipe.fisa.silber.activities.ListBankStatementActivity;

/**
 * Created by zouhairhajji on 17/11/2017.
 */

public class OnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "OnNavigationItemSele...";
    private static Activity currentactivity;



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(currentactivity == null){
            Log.d(TAG, "onNavigationItemSelected: not application context was detected");
            return false;
        }
        switch (item.getItemId()) {
            case R.id.navigation_home:
                // do something
                return true;
            case R.id.navigation_bankstatement:
                if(!(currentactivity instanceof ListBankStatementActivity)){
                    Intent intent = new Intent(currentactivity, ListBankStatementActivity.class);
                    currentactivity.startActivity(intent);
                }
                return true;
        }
        Log.d(TAG, "onNavigationItemSelected: selected an item");
        return false;
    }


    public static void setContext(Activity activity) {
        currentactivity = activity;
    }
}
