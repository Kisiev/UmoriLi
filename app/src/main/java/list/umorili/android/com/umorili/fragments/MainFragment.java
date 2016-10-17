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
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import list.umorili.android.com.umorili.ConstantManager;
import list.umorili.android.com.umorili.rest.UmoriliApi;
import list.umorili.android.com.umorili.rest.models.GetListModel;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.rest.RestService;
import retrofit2.http.GET;


@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment {


    RecyclerView recyclerView;
    @ViewById(R.id.checkbox)
    CheckBox checkBox;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LOG", "createview");

        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }



    @Background
    public void loadEntity() {
        Log.d("LOG", "loader");
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
       recyclerView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                   int id = view.getId();
                   Log.d("ID", String.valueOf(id));
                   MainEntity.update(id);

           }
       });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LOG", "start");
        loadEntity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LOG", "create");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("LOG", "stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG", "destroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("LOG", "destroyview");
    }


}
