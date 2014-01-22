package se.xjobb.scardfeud;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Svempa on 2014-01-22.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Specify that GcmIntentService will handle intent.
        ComponentName componentName = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service
        // Keeping the device awake while launching.
        startWakefulService(context, (intent.setComponent(componentName)));
        setResultCode(Activity.RESULT_OK);
    }
}
