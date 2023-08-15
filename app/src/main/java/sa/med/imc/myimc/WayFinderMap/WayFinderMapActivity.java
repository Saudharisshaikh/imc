package sa.med.imc.myimc.WayFinderMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import sa.med.imc.myimc.Base.BaseActivity;
import sa.med.imc.myimc.R;

public class WayFinderMapActivity extends BaseActivity {

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        finish();
        WayFinderMapActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, WayFinderMapActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WayFinderMapActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_finder_map);
    }
}
