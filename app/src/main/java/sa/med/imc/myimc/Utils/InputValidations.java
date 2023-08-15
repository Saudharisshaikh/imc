package sa.med.imc.myimc.Utils;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;


public class InputValidations {
    public final static Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");


    public static boolean isInputNotEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (!text.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isInputNotEmail(String email) {
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isInputPasswordValid(String pass) {
        if (PASSWORD_PATTERN.matcher(pass).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isInputMatches(EditText input1, EditText input2) {
        String value1 = input1.getText().toString().trim();
        String value2 = input2.getText().toString().trim();
        if (!value1.isEmpty() && !value2.isEmpty()) {
            if (value1.equals(value2)) {
                return true;
            }

        }
        return false;
    }
}
