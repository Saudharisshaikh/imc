package sa.med.imc.myimc.RetailModule.View.Model;

import android.content.Context;
import android.widget.EditText;

import sa.med.imc.myimc.R;

public class AddressValidation {

    Context context;

    public AddressValidation(Context context) {
        this.context = context;
    }

    public boolean isPhoneValid(EditText etMobileNumber) {
        if (etMobileNumber.getText().toString().trim().length() == 0) {
            etMobileNumber.setError(context.getString(R.string.required));
            return false;
        } else if (etMobileNumber.getText().toString().trim().length() < 10) {
            etMobileNumber.setError(context.getString(R.string.enter_valid_phone));
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


    public boolean isPinValid(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            view.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public boolean isHNOValid(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            view.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public boolean isRoadValid(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            view.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

    public boolean isCityStateValid(EditText view) {
        if (view.getText().toString().trim().length() == 0) {
            view.setError(context.getString(R.string.required));
            return false;
        } else {
            return true;
        }
    }

}
