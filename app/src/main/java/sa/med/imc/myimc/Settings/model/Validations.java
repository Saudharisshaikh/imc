package sa.med.imc.myimc.Settings.model;

import android.content.Context;
import android.widget.EditText;

import sa.med.imc.myimc.R;
import sa.med.imc.myimc.Utils.InputValidations;

public class Validations {
    Context context;

    public Validations(Context context) {
        this.context = context;
    }


    public boolean isMsgValid(EditText messge) {
        if (messge.getText().toString().trim().length() == 0) {
            messge.setError(context.getString(R.string.required));
            return false;
        } else if (messge.getText().toString().trim().length() < 20) {
            messge.setError(context.getString(R.string.enter_some_more_text));
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

    public boolean isPhoneValid(EditText etPhone) {
        if (etPhone.getText().toString().trim().length() == 0) {
            etPhone.setError(context.getString(R.string.required));
            return false;
        } else if (etPhone.getText().toString().trim().length() < 9) {
            etPhone.setError(context.getString(R.string.enter_valid_phone));
            return false;
        } else {
            return true;
        }
    }

    public boolean isNameValid(EditText etName) {
        if (etName.getText().toString().trim().length() == 0) {
            etName.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }
}
