package sa.med.imc.myimc.AddGuardian.model;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.InputValidations;

/*
 * Validation for Consent forms fields.
 */

public class ConsentForm1Validation {

    Context context;

    public ConsentForm1Validation(Context context) {
        this.context = context;

    }


    public boolean isEnValid(EditText etEnNumber) {
        if (etEnNumber.getText().toString().trim().length() == 0) {
            etEnNumber.setError(context.getString(R.string.required));
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

    public boolean isPatientNameValid(EditText etName) {
        if (etName.getText().toString().trim().length() == 0) {
            etName.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
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


    public boolean isFromDateValid(TextView tvFromDate) {
        if (tvFromDate.getText().toString().trim().length() == 0) {
            tvFromDate.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }


    public boolean isToDateValid(TextView tvToDate) {
        if (tvToDate.getText().toString().trim().length() == 0) {
            tvToDate.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

}
