package list.umorili.android.com.umorili.adapters;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.FavoriteEntity;


import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.fragments.FavoriteFragment;



public class MainFragtentAdapter extends RecyclerView.Adapter<MainFragtentAdapter.MainFragmentHolder> {

    private List<MainEntity> mainEntityList;
    public View viewForPager;
    RecyclerView recyclerView;

    private void setFavoriteList(final MainFragmentHolder holder) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {

                        if (compoundButton.isChecked()) {
                            if ((SQLite.select().from(FavoriteEntity.class).where(FavoriteEntity_Table.id_list.eq(mainEntityList.get(holder.getAdapterPosition()).getId())).queryList().isEmpty())) {
                                FavoriteEntity.insert(mainEntityList.get(holder.getAdapterPosition()).getId(), mainEntityList.get(holder.getAdapterPosition()).getList());
                                MainEntity.updateFavorite(mainEntityList.get(holder.getAdapterPosition()).getId(), true);

                            }

                        } else {
                            FavoriteEntity.delete(mainEntityList.get(holder.getAdapterPosition()).getId());
                            MainEntity.updateFavorite(mainEntityList.get(holder.getAdapterPosition()).getId(), false);
                        }
                        mainEntityList = MainEntity.listUmor();
                        FavoriteFragment.adapter.favoriteEntityList = null;

                        FavoriteFragment.adapter = new FavoriteFragmentAdapter(FavoriteEntity.selectedALL(), FavoriteFragment.adapter.clickListener, FavoriteFragment.context);
                        FavoriteFragment.recyclerView.setAdapter(FavoriteFragment.adapter);

                    }
                });
            }
        });

    }

    public MainFragtentAdapter(List<MainEntity> mainEntity) {
        this.mainEntityList = mainEntity;
    }

    @Override
    public MainFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_fragment_item, parent, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.favorite_fragment_recycler);
        return new MainFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainFragmentHolder holder, int position) {
        MainEntity mainEntity = mainEntityList.get(position);

        holder.name.setText(mainEntity.getList());
        holder.checkBox.setChecked(mainEntity.isFavorite());
        setFavoriteList(holder);
    }

    @Override
    public int getItemCount() {
        return mainEntityList.size();
    }


    class MainFragmentHolder extends RecyclerView.ViewHolder {

        TextView name;
        CheckBox checkBox;

        public MainFragmentHolder(View item) {
            super(item);
            name = (TextView) item.findViewById(R.id.name_item);
            checkBox = (CheckBox) item.findViewById(R.id.checkbox);
        }

    }

}
