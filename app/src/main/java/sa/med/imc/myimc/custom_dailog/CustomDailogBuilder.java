package sa.med.imc.myimc.custom_dailog;

import android.content.Context;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import sa.med.imc.myimc.R;

public class CustomDailogBuilder {
    Context context;
    Dialog dialog;

    public CustomDailogBuilder(Context context) {
        this.context = context;
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.custom_dailo_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        dialog.setCancelable(false);
    }
    public void show(){
        dialog.show();
    }

    public void dismiss(){
        try {
            dialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        if (dialog.isShowing()){
            return true;
        }else return false;
    }
}
