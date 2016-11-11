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

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.androidannotations.annotations.Background;

import java.util.List;

import list.umorili.android.com.umorili.MainActivity;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.FavoriteEntity;


import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
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

        holder.name.setText(mainEntity.getList());
        holder.checkBox.setChecked(mainEntity.isFavorite());
        holder.time.setText(mainEntity.getTime());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //selected_id = mainEntityList.get(position).getId();
                if (holder.checkBox.isChecked()) {
                    if(SQLite.select().from(FavoriteEntity.class).where(FavoriteEntity_Table.id_list.eq(mainEntityList.get(position).getId())).queryList().isEmpty())
                    FavoriteEntity.insert(mainEntityList.get(position).getId(), mainEntityList.get(position).getList());

                }else {
                    FavoriteEntity.delete(mainEntityList.get(position).getId());
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
        TextView time;
        public MainFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item);
            checkBox = (CheckBox) item.findViewById(R.id.checkbox);
            time = (TextView) item.findViewById(R.id.time);
        }

    }
    public String getPos(){
        return selectedItem;
    }
}
