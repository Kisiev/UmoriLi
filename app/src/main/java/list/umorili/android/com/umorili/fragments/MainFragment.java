package list.umorili.android.com.umorili.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.MainActivity;
import list.umorili.android.com.umorili.MainActivity_;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.SettingActivity_;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.rest.RestService;
import list.umorili.android.com.umorili.rest.models.GetListModel;

@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment {
    public static RestService restService = new RestService();
    public static List<GetListModel> getListModels;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewById(R.id.checkbox)
    CheckBox checkBox;
    public static RecyclerView recyclerView;
    public static Context context;
    MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        loadMainEntity();
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    public void loadMainEntity() {
        mainActivity = new MainActivity(getListModels, restService);
        mainActivity.delete();
        mainActivity.loadMainEntity();
        loadEntity();
    }

    public void loadEntity() {
        try {

            getLoaderManager().restartLoader(ConstantManager.ID, null, new LoaderManager.LoaderCallbacks<List<MainEntity>>() {
                @Override
                public Loader<List<MainEntity>> onCreateLoader(int id, Bundle args) {
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
                public void onLoadFinished(Loader<List<MainEntity>> loader, List<MainEntity> data) {
                    recyclerView.setAdapter(new MainFragtentAdapter(data));

                }

                @Override
                public void onLoaderReset(Loader<List<MainEntity>> loader) {
                }
            });
        } catch (RuntimeException e) {

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        context = getActivity();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMainEntity();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.observer.addOnTableChangedListener(new FlowContentObserver.OnTableChangedListener() {
            @Override
            public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {
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
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(getActivity(), SettingActivity_.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
