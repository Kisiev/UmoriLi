package list.umorili.android.com.umorili;

import android.content.SharedPreferences;
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
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;

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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import list.umorili.android.com.umorili.adapters.NewPagerFragmentAdapter;
import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.fragments.FavoriteFragment_;
import list.umorili.android.com.umorili.fragments.MainFragment_;
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
    public static FlowContentObserver observer = new FlowContentObserver();
   ViewPager viewPager;

    @ViewById
    TabHost tabHost;
    @ViewById
    Toolbar toolbar;


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

        String[] tabName = {"Главное", "Избранное"};

        for (int i = 0; i < tabName.length; i ++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabName[i]);
            tabSpec.setIndicator(tabName[i]);
            tabSpec.setContent(new ContentFactory(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);

    }


    private void replaceFragment(Fragment fragment, int id) {
        String backStackName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(id, fragment, backStackName);
            ft.addToBackStack(backStackName);
            ft.commit();
        }


    }

    private void initStetho(){
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
       /* MainFragment mainFragment = new MainFragment();
        replaceFragment(mainFragment, R.id.tab1);
        setTitle(getString(R.string.history_title));
        tabHost.setCurrentTabByTag(TAG1);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s) {
                    case TAG1:
                        MainFragment mainFragment = new MainFragment();
                        replaceFragment(mainFragment, R.id.tab1);
                        setTitle(getString(R.string.history_title));
                        break;
                    case TAG2:

                        FavoriteFragment favoriteFragment = new FavoriteFragment();
                        replaceFragment(favoriteFragment, R.id.tab2);
                        setTitle(getString(R.string.favorite));
                        break;
                }
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        delete();
        loadMainEntity();
    }






    @Override
    public void onBackPressed() {
        finish();
    }

    @Background
    public void loadMainEntity() {

        try {
            getListModels = (restService.viewListInMainFragmenr(ConstantManager.SITE, ConstantManager.NAME, ConstantManager.NUM));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
