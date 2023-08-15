package sa.med.imc.myimc.SignUp.Model;


import android.content.Context;
import android.widget.EditText;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.InputValidations;

/*
 * Validation for Register mobile number
 * Validations for sign up fields
 */
public class Validations {

    Context context;

    public Validations(Context context) {
        this.context = context;
    }

    public boolean isPhoneValid(EditText etMobileNumber) {
        if (etMobileNumber.getText().toString().trim().length() == 0) {
            etMobileNumber.setError(context.getString(R.string.required));
            return false;
        } else if (etMobileNumber.getText().toString().trim().length() < 9) {
            etMobileNumber.setError(context.getString(R.string.enter_valid_phone));
            return false;
        } else {
            return true;
        }
    }

    public boolean isEmailValid(EditText etEmailId) {
        if (etEmailId.getText().toString().trim().length() == 0) {
            etEmailId.setError(context.getString(R.string.required));
            return false;
        } else if (!InputValidations.isInputNotEmail(etEmailId.getText().toString().trim())) {
            etEmailId.setError(context.getString(R.string.enter_valid_email));
            return false;
        } else {
            return true;
        }
    }

    public boolean isFNameValid(EditText etName) {
        if (etName.getText().toString().trim().length() == 0) {
            etName.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public boolean isLNameValid(EditText etName) {
        if (etName.getText().toString().trim().length() == 0) {
            etName.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public boolean isMRNValid(EditText etMRN) {
        if (etMRN.getText().toString().trim().length() == 0) {
            etMRN.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

}
