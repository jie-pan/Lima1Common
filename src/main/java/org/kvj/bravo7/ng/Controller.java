package org.kvj.bravo7.ng;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.kvj.bravo7.log.AndroidLogger;
import org.kvj.bravo7.log.Logger;
import org.kvj.bravo7.ng.conf.Configurator;
import org.kvj.bravo7.ng.conf.SharedPreferencesConfigurable;

import java.util.Date;

/**
 * Created by kvorobyev on 4/8/15.
 */

public class Controller {

    protected final Context context;
    protected Logger logger = Logger.forInstance(this);

    public Controller(Context context, String name) {
        Logger.setOutput(new AndroidLogger(name));
        this.context = context;
    }

    public Configurator settings() {
        return new Configurator(new SharedPreferencesConfigurable(context, PreferenceManager.getDefaultSharedPreferences(context)));
    }

    public SharedPreferences preferences(String s) {
        return context.getSharedPreferences(s, Context.MODE_PRIVATE);
    }

    public Configurator settings(String s) {
        return new Configurator(new SharedPreferencesConfigurable(context, preferences(s)));
    }

    public Context context() {
        return context;
    }

    public void messageShort(String message) {
        logger.w("Toast:", message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void messageLong(String message) {
        logger.w("Toast:", message);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void question(Context context, String message, final Runnable yesHandler, final Runnable noHandler) {
        new AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != yesHandler) yesHandler.run();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != noHandler)
                    noHandler.run();
            }
        }).show();
    }

    public PowerManager.WakeLock lock() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock lock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Lima1");
        return lock;
    }

    public void cancelAlarm(PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
    }

    public void scheduleAlarm(Date when, PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, when.getTime(), intent);
    }
}
