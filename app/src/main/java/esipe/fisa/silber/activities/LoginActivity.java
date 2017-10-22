package esipe.fisa.silber.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Bind;
import esipe.fisa.silber.esipe.fisa.silber.services.UserService;
import esipe.fisa.silber.utils.HttpUtil;
import esipe.fisa.silber.validators.InputValidator;

import static esipe.fisa.silber.validators.InputValidator.EMAIL;
import static esipe.fisa.silber.validators.InputValidator.OTHER;
import static esipe.fisa.silber.validators.InputValidator.validateEmail;
import static esipe.fisa.silber.validators.InputValidator.validatePassword;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(com.fisa.silber.R.id.input_email)
    EditText _emailText;

    @Bind(com.fisa.silber.R.id.input_password)
    EditText _passwordText;

    @Bind(com.fisa.silber.R.id.btn_login)
    Button _loginButton;

    @Bind(com.fisa.silber.R.id.link_signup)
    TextView _signupLink;

    private UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.fisa.silber.R.layout.activity_login);
        this.userService = new UserService();
        ButterKnife.bind(this);

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(com.fisa.silber.R.anim.push_left_in, com.fisa.silber.R.anim.push_left_out);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO: Define the activity
        //moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //Redirection to the main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        ;
        ;
        return valid;
    }

    public void btnClickLogin(View view)  {
        Log.d(TAG, "Login");
        _loginButton.setEnabled(false);

        if ( !validateEmail(_emailText) ||  !validatePassword(_passwordText) ) {
            onLoginFailed();
            return;
        }




        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("http://silber.portail.silber.inside.esiag.info/silber_portail/login", "GET");


    }


    /**
     *
     * Call WebService using async
     */


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private ProgressDialog progressDialog;
        private HttpUtil httpUtil;
        @Override
        protected String doInBackground(String... params) {
            httpUtil = new HttpUtil();
            try {
                httpUtil.initConnection(params[0]);
                httpUtil.setMethod(params[1]);
                httpUtil.connect();
                httpUtil.getCookie();

            } catch (IOException e) {
                Log.d(TAG, "EXCEPTION: " + e.getMessage());
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }
                    }, 20);
            _loginButton.setEnabled(true);
        }


        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(LoginActivity.this, "ProgressDialog", "Authentification");
        }
    }
}
