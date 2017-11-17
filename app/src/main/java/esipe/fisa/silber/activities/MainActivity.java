package esipe.fisa.silber.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fisa.silber.R;

import esipe.fisa.silber.listeners.OnNavigationItemSelectedListener;


public class MainActivity extends AppCompatActivity {

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fisa.silber.R.layout.activity_main);

        OnNavigationItemSelectedListener.setContext(this);
        this.mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.fisa.silber.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_home){
            return true;
        }
        else if (id == R.id.action_logout){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == com.fisa.silber.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
