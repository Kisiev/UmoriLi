package list.umorili.android.com.umorili;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import list.umorili.android.com.umorili.sync.BashJobCreater;


public class UmoriliAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        JobManager.create(this).addJobCreator(new BashJobCreater());
    }
}
