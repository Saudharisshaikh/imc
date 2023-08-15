package sa.med.imc.myimc.MedicineDetail;

import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ComponentName comp = new ComponentName(context.getPackageName(), AlarmNotificationService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, AlarmNotificationService.class));
        } else {
            context.startService(new Intent(context, AlarmNotificationService.class));
        }

    }
}
