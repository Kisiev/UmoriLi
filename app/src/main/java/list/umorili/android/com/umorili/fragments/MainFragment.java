package list.umorili.android.com.umorili.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;


@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment {

    public static final int ID = 1;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    @Background
    public void loadEntity() {
        getLoaderManager().restartLoader(ID, null, new LoaderManager.LoaderCallbacks<List<MainEntity>>() {
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
        loadEntity();
    }
}
