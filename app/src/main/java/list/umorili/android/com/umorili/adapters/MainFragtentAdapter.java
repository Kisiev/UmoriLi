package list.umorili.android.com.umorili.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.MainEntity;


public class MainFragtentAdapter extends RecyclerView.Adapter<MainFragtentAdapter.MainFragmentHolder>{
    private String selectedItem;
    MainEntity mainEntity;
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
    public void onBindViewHolder(final MainFragmentHolder holder, final int position) {
        mainEntity = mainEntityList.get(position);
        selectedItem = mainEntityList.get(position).getId();

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.checkBox.isChecked()) {
                    MainEntity.update(selectedItem);
                    Log.d("LOF", "ЗАШЕЛ");
                    mainEntity.save();
                } else holder.checkBox.setChecked(false);
            }
        });
        holder.name.setText(mainEntity.getList());
        holder.checkBox.setChecked(mainEntity.isFavorite());
    }

    @Override
    public int getItemCount() {
        return mainEntityList.size();
    }

    class MainFragmentHolder extends RecyclerView.ViewHolder{

        TextView name;
        CheckBox checkBox;
        public MainFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item);
            checkBox = (CheckBox) item.findViewById(R.id.checkbox);
        }

    }
    public String getPos(){
        return selectedItem;
    }
}
