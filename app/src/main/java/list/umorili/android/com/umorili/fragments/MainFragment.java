package list.umorili.android.com.umorili.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.MainActivity;
import list.umorili.android.com.umorili.MainActivity_;
import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.rest.UmoriliApi;
import list.umorili.android.com.umorili.rest.models.GetListModel;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.rest.RestService;
import retrofit2.http.GET;


@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment{

    SwipeRefreshLayout mSwipeRefreshLayout;
    FlowContentObserver observer = new FlowContentObserver();
    RecyclerView recyclerView;
    @ViewById(R.id.checkbox)
    CheckBox checkBox;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        observer.registerForContentChanges(getActivity(), MainEntity.class);
        observer.addOnTableChangedListener(new FlowContentObserver.OnTableChangedListener() {
            @Override
            public void onTableChanged(@Nullable Class<? extends Model> tableChanged, BaseModel.Action action) {
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
            }

            @Override
            public void onLoaderReset (Loader < List < MainEntity >> loader) {
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadEntity();
    }


}
