package list.umorili.android.com.umorili.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.adapters.MainFragtentAdapter;
import list.umorili.android.com.umorili.models.MainModel;

@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment{

    RecyclerView recyclerView;

    MainFragtentAdapter mainFragtentAdapter;

    private List<MainModel> getModel(){
        List<MainModel> mainModels = new ArrayList<>();
            mainModels.add(new MainModel("afasf"));
            mainModels.add(new MainModel("faaga"));
        return mainModels;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.main_fragment_recycler);
        mainFragtentAdapter = new MainFragtentAdapter(getModel());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mainFragtentAdapter);
        return view;
    }
}
