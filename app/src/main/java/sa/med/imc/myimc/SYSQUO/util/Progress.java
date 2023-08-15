package sa.med.imc.myimc.SYSQUO.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import sa.med.imc.myimc.R;


public class Progress extends ProgressDialog {

    public Progress(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysquo_activity_progres);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
