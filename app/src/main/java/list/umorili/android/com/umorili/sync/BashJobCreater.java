package list.umorili.android.com.umorili.sync;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;


public class BashJobCreater implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case BashSyncJob.TAG:
                return new BashSyncJob();
            default:
                return null;
        }

    }

}
