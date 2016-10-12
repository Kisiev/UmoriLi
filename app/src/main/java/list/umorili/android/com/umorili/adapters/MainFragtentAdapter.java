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
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.models.MainModel;


public class MainFragtentAdapter extends RecyclerView.Adapter<MainFragtentAdapter.MainFragmentHolder>{

    public List<MainEntity> mainEntityList;
    public MainFragtentAdapter (List<MainEntity> mainEntity) {
        this.mainEntityList = mainEntity;
    }
    @Override
    public MainFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_fragment_item, parent, false);
        return new MainFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(MainFragmentHolder holder, int position) {
        MainEntity mainEntity = mainEntityList.get(position);
        holder.name.setText(mainEntity.getName());
        holder.radioButton.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mainEntityList.size();
    }

    class MainFragmentHolder extends RecyclerView.ViewHolder{

        TextView name;
        RadioButton radioButton;
        public MainFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item);
            radioButton = (RadioButton) item.findViewById(R.id.radio);
        }

    }
}
