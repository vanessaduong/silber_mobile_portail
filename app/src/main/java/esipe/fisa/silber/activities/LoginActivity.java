package esipe.fisa.silber.activities;

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
import esipe.fisa.silber.listeners.OnNavigationItemSelectedListener;

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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnNavigationItemSelectedListener.setContext(this);
        super.onCreate(savedInstanceState);
        setContentView(com.fisa.silber.R.layout.activity_login);
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
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        return valid;
    }

    public void btnClickLogin(View view) throws IOException {
        Log.d(TAG, "Login");


        Log.d(TAG, "btnClickLogin: 0");
        if (!validateEmail(_emailText) || !validatePassword(_passwordText)) {
            onLoginFailed();
            return;
        }
        Log.d(TAG, "btnClickLogin: 1");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        //BankStatementService bankStatementService = new BankStatementService(this.getApplicationContext());
        //bankStatementService.downloadBankStatementPDF(4);



        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
