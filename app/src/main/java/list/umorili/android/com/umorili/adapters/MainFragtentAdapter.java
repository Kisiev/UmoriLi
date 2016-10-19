package list.umorili.android.com.umorili.adapters;

import android.os.Handler;
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

import org.androidannotations.annotations.Background;

import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.MainEntity;


public class MainFragtentAdapter extends RecyclerView.Adapter<MainFragtentAdapter.MainFragmentHolder>{
    private String selectedItem;
    FavoriteEntity favoriteEntity;

    public static String getSelected_id() {
        return selected_id;
    }

    private static String selected_id = null;
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
        selected_id = mainEntityList.get(position).getId();
        holder.name.setText(mainEntity.getList());
        holder.checkBox.setChecked(mainEntity.isFavorite());
        /*holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(true);
                    Log.d("ADAPTER",mainEntity.getList());
                    FavoriteEntity.insert(mainEntity.getList());
                }else {
                    holder.checkBox.setChecked(false);

                }

            }
        });*/

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBox.isChecked()) {
                    FavoriteEntity.insert(selected_id);
                }
            }
        });

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
