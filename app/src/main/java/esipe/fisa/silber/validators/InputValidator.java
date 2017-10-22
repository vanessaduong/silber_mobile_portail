package esipe.fisa.silber.validators;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zouhairhajji on 22/10/2017.
 */

public class InputValidator {

    public final static String EMAIL = "EMAIL";
    public final static String OTHER = "OTHER";

    public static boolean validatePassword(EditText input){
        String str = input.getText().toString();
        int maxLength = 10;
        int minLength = 4;
        if (str.isEmpty()  || str.length() < 4 || str.length() > 10) {
            input.setError(String.format("Between %d and %d alphanumeric characters", minLength, maxLength));
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }

    public static boolean validateEmail(EditText email){
        String _email = email.getText().toString();
        if ( _email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("Enter a valid email address");
            return false;
        } else {
            return true;
        }
    }

}
