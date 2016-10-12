package list.umorili.android.com.umorili.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import org.androidannotations.annotations.ViewById;

import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.models.MainModel;


public class MainFragtentAdapter extends RecyclerView.Adapter<MainFragtentAdapter.MainFragmentHolder>{

    public List<MainModel> mainModelList;
    public MainFragtentAdapter (List<MainModel> mainModel) {
        this.mainModelList = mainModel;
    }
    @Override
    public MainFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_fragment_item, parent, false);
        return new MainFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(MainFragmentHolder holder, int position) {
        MainModel mainModel = mainModelList.get(position);
        holder.name.setText(mainModel.getName());
    }

    @Override
    public int getItemCount() {
        return mainModelList.size();
    }

    class MainFragmentHolder extends RecyclerView.ViewHolder{

        TextView name;
        public MainFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item);
        }

    }
}
