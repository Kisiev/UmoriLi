package list.umorili.android.com.umorili;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import list.umorili.android.com.umorili.adapters.NewPagerFragmentAdapter;
import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.rest.models.GetListModel;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.fragments.FavoriteFragment;
import list.umorili.android.com.umorili.fragments.MainFragment;
import list.umorili.android.com.umorili.rest.RestService;
import list.umorili.android.com.umorili.sync.BashSyncJob;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    public static RestService restService = new RestService();
    public static List<GetListModel> getListModels;
    private static SharedPreferences sharedPreferences;
    public static String service_setting;
    private static SwipeRefreshLayout swipeRefreshLayout;
    public static FlowContentObserver observer = new FlowContentObserver();
    ViewPager viewPager;

    @ViewById
    TabHost tabHost;
    @ViewById
    Toolbar toolbar;

    public MainActivity() {
    }

    public MainActivity(List<GetListModel> getListModelList, RestService service ) {
        getListModels = getListModelList;
        restService = service;;
    }

    public void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> fragments = new ArrayList<>();
        NewPagerFragmentAdapter pagerFragmentAdapter;
        fragments.add(new MainFragment());
        fragments.add(new FavoriteFragment());

        viewPager.setOffscreenPageLimit(2);
        pagerFragmentAdapter = new NewPagerFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerFragmentAdapter);

        viewPager.setOnPageChangeListener(this);
    }


    private void clearJobSync() {
        int idJob = BashSyncJob.schedulePeriodicJob();
        for (int i = 1; i < idJob; i++)
            BashSyncJob.cancelJob(i);
    }

    private void initTab() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        String[] tabName = {getString(R.string.main_tab), getString(R.string.favorite_tab)};

        for (int i = 0; i < tabName.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabName[i]);
            tabSpec.setIndicator(tabName[i]);
            tabSpec.setContent(new ContentFactory(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);

    }


    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .build());
    }

    public static String replaceSimbolInText(String text) {
        String newText = text.replace("<br />", "");
        newText = newText.replace("&lt;", "<");
        newText = newText.replace("&gt;", ">");
        newText = newText.replace("&quot;", "''");
        newText = newText.replace("&amp;", "&");
        newText = newText.replace("&nbsp;", " ");
        newText = newText.replace("&laquo;", "''");
        newText = newText.replace("&raquo;", "''");
        newText = newText.replace("&curren;", "|");

        return newText;
    }

    @AfterViews
    public void main() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        observer.registerForContentChanges(this, MainEntity.class);

        initStetho();
        clearJobSync();
        setSupportActionBar(toolbar);
        tabHost.setup();
        initViewPager();
        initTab();
        service_setting = sharedPreferences.getString(getString(R.string.pref_setting_service), getString(R.string.news));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(sharedPreferences.getString(getString(R.string.pref_setting_service), getString(R.string.news_title_array)));
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void loadMainEntity() {

        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getListModels = (restService.viewListInMainFragmenr(ConstantManager.SITE, ConstantManager.NAME, ConstantManager.NUM));
                    final List<GetListModel> quotes = getListModels;
                    FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {

                        @Override
                        public void execute(DatabaseWrapper databaseWrapper) {
                            MainEntity quoteEntity = new MainEntity();
                            for (GetListModel quote : quotes) {
                                quoteEntity.setId(quote.getLink());
                                quoteEntity.setList(replaceSimbolInText(quote.getElementPureHtml()));
                                if ((SQLite.select().from(FavoriteEntity.class).where(FavoriteEntity_Table.id_list.eq(quote.getLink())).queryList().size() <= 0))
                                    quoteEntity.setFavorite(false);
                                else quoteEntity.setFavorite(true);
                                quoteEntity.save(databaseWrapper);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        newThread.start();
        try {
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void delete() {
        MainEntity.deleteAll();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String s) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

    }

}
