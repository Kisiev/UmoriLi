package list.umorili.android.com.umorili.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.LongProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.MainActivity;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;

import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.entity.MainEntity_Table;
import list.umorili.android.com.umorili.rest.RestService;
import list.umorili.android.com.umorili.rest.models.GetListModel;


public class BashSyncJob extends Job {

    private static final long[] VIBRATE_PATTERN = new long[]{0, 100, 0, 200};
    private static final int LED_LIGHTS_TIME_ON = 200;
    private static final int LED_LIGHTS_TIME_OFF = 1500;
    private static final int NOTIFICATION_ID = 4005;
    private NotificationManager mNotificationManager;
    private boolean isNotificationsEnabled;
    private boolean isVibrateEnabled;
    private boolean isSoundEnabled;
    private boolean isLedEnabled;

    private SharedPreferences mSharedPreferences;
    private String globalNotificationsKey;
    private String vibrateNotificationsKey;
    private String soundNotificationsKey;
    private String ledNotificationsKey;
    private static final boolean DEFAULT_VALUE = true;
    private static int countNotify = 1;
    public static final String TAG = "job_demo_tag";
    public static RestService restService = new RestService();
    public static List<GetListModel> getListModels;

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        load();
        if (countNotify > 1) sendNotification(countNotify);
        countNotify = 1;
        return Result.SUCCESS;
    }

    public static int schedulePeriodicJob() {
        return new JobRequest.Builder(BashSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setPersisted(true)
                .build()
                .schedule();
    }

    public void loadMainEntity(final List<GetListModel> quotes) throws IOException {
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                MainEntity quoteEntity = new MainEntity();
                for (GetListModel quote : quotes) {
                        quoteEntity.setId(quote.getLink());
                        quoteEntity.setList(quote.getElementPureHtml());
                        if (SQLite.select().from(FavoriteEntity.class).where(FavoriteEntity_Table.id_list.eq(quote.getLink())).queryList().size() <= 0)
                            quoteEntity.setFavorite(false);
                        else quoteEntity.setFavorite(true);
                        quoteEntity.save(databaseWrapper);
                        countNotify++;
                }

            }
        });
    }

    void load() {
        try {
            getListModels = (restService.viewListInMainFragmenr(ConstantManager.SITE, ConstantManager.NAME, ConstantManager.NUM));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            loadMainEntity(getListModels);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void delete() {
        MainEntity.deleteAll();
    }

    public static void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }

    private void init() {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        globalNotificationsKey = getContext().getString(R.string.pref_enable_notifications_key);
        vibrateNotificationsKey = getContext().getString(R.string.pref_enable_vibrate_notifications_key);
        soundNotificationsKey = getContext().getString(R.string.pref_enable_sound_notifications_key);
        ledNotificationsKey = getContext().getString(R.string.pref_enable_led_notifications_key);


        isNotificationsEnabled = DEFAULT_VALUE;
        isVibrateEnabled = mSharedPreferences.getBoolean(vibrateNotificationsKey, DEFAULT_VALUE);
        isSoundEnabled = mSharedPreferences.getBoolean(soundNotificationsKey, DEFAULT_VALUE);
        isLedEnabled = mSharedPreferences.getBoolean(ledNotificationsKey, DEFAULT_VALUE);
    }

    public void sendNotification(int notifyCount) {
        init();
        if (!isNotificationsEnabled) {
            return;
        }
        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.bash_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bash_icon))
                .setContentTitle(getContext().getString(R.string.app_name))
                .setContentText(getContext().getString(R.string.notification_message))
                .setNumber(notifyCount)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (isLedEnabled) {
            builder.setLights(Color.BLUE, LED_LIGHTS_TIME_ON, LED_LIGHTS_TIME_OFF);
        }

        if (isSoundEnabled) {
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }

        if (isVibrateEnabled) {
            builder.setVibrate(VIBRATE_PATTERN);
        }

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
