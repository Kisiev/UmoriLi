package list.umorili.android.com.umorili.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TabWidget;
import android.widget.Toast;

import com.evernote.android.job.JobManager;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.DefaultTransactionManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import list.umorili.android.com.umorili.ConstantManager;

import list.umorili.android.com.umorili.MainActivity;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.SettingActivity;
import list.umorili.android.com.umorili.SettingActivity_;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;

@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
   // FlowContentObserver observer = new FlowContentObserver();
    @ViewById(R.id.checkbox)
    CheckBox checkBox;
    public static RecyclerView recyclerView;
    public static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainFragment", "onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ONSTART", "onCreateView");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadEntity();
            }
        });

        return view;
    }


    @Background
    public void loadEntity() {
        getLoaderManager().restartLoader(ConstantManager.ID, null, new LoaderManager.LoaderCallbacks<List<MainEntity>>() {
            @Override
            public Loader<List<MainEntity>> onCreateLoader ( int id, Bundle args){
                final AsyncTaskLoader<List<MainEntity>> loader = new AsyncTaskLoader<List<MainEntity>>(getActivity()) {
                    @Override
                    public List<MainEntity> loadInBackground() {
                        return MainEntity.listUmor();
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished (Loader < List < MainEntity >> loader, List < MainEntity > data){
                recyclerView.setAdapter(new MainFragtentAdapter(data));
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoaderReset (Loader < List < MainEntity >> loader) {
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        context = getActivity();
        loadEntity();
        Log.d("ONSTART", "OnStart");
    }

    @Override
    public void onResume() {
        super.onResume();
       // MainActivity.observer.registerForContentChanges(getActivity(), MainEntity.class);
        MainActivity.observer.addOnTableChangedListener(new FlowContentObserver.OnTableChangedListener() {
            @Override
            public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {
                Log.d("OBERV", "DAAAA");
                loadEntity();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                Intent intent = new Intent(getActivity(), SettingActivity_.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadEntity();
    }
}
