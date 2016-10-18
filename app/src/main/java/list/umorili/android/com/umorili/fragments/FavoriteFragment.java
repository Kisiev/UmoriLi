package list.umorili.android.com.umorili.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.zip.Inflater;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.FavoriteFragmentAdapter;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;

@EFragment(R.layout.favorite_fragment)
public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    FlowContentObserver observer = new FlowContentObserver();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_fragment_recycler);
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
            public Loader<List<MainEntity>> onCreateLoader (int id, Bundle args){
                final AsyncTaskLoader<List<MainEntity>> loader = new AsyncTaskLoader<List<MainEntity>>(getActivity()) {
                    @Override
                    public List<MainEntity> loadInBackground() {
                        return MainEntity.listUmorFavorite();
                    }
                };
                loader.forceLoad();
                return loader;
            }

            @Override
            public void onLoadFinished (Loader < List < MainEntity >> loader, List < MainEntity > data){
                recyclerView.setAdapter(new FavoriteFragmentAdapter(data));
            }

            @Override
            public void onLoaderReset (Loader < List < MainEntity >> loader) {
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        loadEntity();
    }
}
