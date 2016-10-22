package list.umorili.android.com.umorili;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
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
import java.util.Random;

import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.rest.models.GetListModel;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.fragments.FavoriteFragment;
import list.umorili.android.com.umorili.fragments.MainFragment;
import list.umorili.android.com.umorili.rest.RestService;


@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG1 = "tag1";
    private final static String TAG2 = "tag2";

    RestService restService = new RestService();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    List<GetListModel> load ;
    @ViewById
    TabHost tabHost;
    @ViewById(R.id.name_item_favorite)
    TextView name_item;
    @ViewById(R.id.main_fragment_recycler)
    RecyclerView recyclerView;
    @ViewById
    Toolbar toolbar;


    @Background
    void load (){

        try {
                load = (restService.viewListInMainFragmenr(ConstantManager.SITE, ConstantManager.NAME, ConstantManager.NUM));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            loadMainEntity(load);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    void delete(){
        MainEntity.deleteAll();
    }
    @AfterViews
    void main (){

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        tabHost.setup();
        setSupportActionBar(toolbar);
        // Вкладка главная
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(TAG1);
        tabSpec.setIndicator(getString(R.string.main_tab));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        // Вкладка избранное
        tabSpec = tabHost.newTabSpec(TAG2);
        tabSpec.setIndicator(getString(R.string.favorite_tab));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
        // по умолчанию показывать главную вкладку
        delete();
        load();
        MainFragment mainFragment = new MainFragment();
        replaceFragment(mainFragment, R.id.tab1);
        setTitle(getString(R.string.history_title));
        tabHost.setCurrentTabByTag(TAG1);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
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
        });

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_bt) {
            FavoriteEntity.deleteAllFavorite();
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadMainEntity(final List<GetListModel> quotes) throws IOException {
       // quotes = restService.viewListInMainFragmenr(ConstantManager.SITE, ConstantManager.NAME, ConstantManager.NUM);
       // if (time != quoteEntity.getTimestp())
        FlowManager.getDatabase(AppDatabase.class).executeTransaction(new ITransaction() {

            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                MainEntity quoteEntity = new MainEntity();
                for (GetListModel quote: quotes) {
                    quoteEntity.setId(quote.getLink());
                    quoteEntity.setList(quote.getElementPureHtml());
                    if (SQLite.select().from(FavoriteEntity.class).where(FavoriteEntity_Table.id_list.eq(quote.getLink())).queryList().size() <= 0 )
                    quoteEntity.setFavorite(false);
                    else  quoteEntity.setFavorite(true);
                    quoteEntity.save(databaseWrapper);
                }

            }
        });


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delete();
                load();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }
}
