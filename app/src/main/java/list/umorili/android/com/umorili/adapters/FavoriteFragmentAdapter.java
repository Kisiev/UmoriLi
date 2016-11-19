package list.umorili.android.com.umorili.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.FavoriteEntity_Table;
import list.umorili.android.com.umorili.entity.MainEntity;
import list.umorili.android.com.umorili.entity.MainEntity_Table;

public class FavoriteFragmentAdapter extends SelectableAdapter<FavoriteFragmentAdapter.FavoriteFragmentHolder>{
    private Context context;
    ClickListener clickListener;
    FavoriteEntity favoriteEntity;
    List<FavoriteEntity> favoriteEntityList;


    public FavoriteFragmentAdapter(List<FavoriteEntity> favoriteEntity, ClickListener clickListener, Context context) {
        this.favoriteEntityList = favoriteEntity;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public FavoriteFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_fragment_item, parent, false);
        return new FavoriteFragmentHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(FavoriteFragmentHolder holder, int position) {
        favoriteEntity = favoriteEntityList.get(position);
        holder.name.setText(favoriteEntity.getList());
        holder.selectedItem.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return favoriteEntityList.size();
    }

    private void removeItem(int position) {

        removeCategory(position);
        notifyItemRemoved(position);

    }

    private void removeCategory(int position) {
        if (favoriteEntityList.get(position) != null) {
            favoriteEntityList.get(position).delete();
            SQLite.update(MainEntity.class).set(MainEntity_Table.favorite.eq(false)).where(MainEntity_Table.content.eq(favoriteEntityList.get(position).getList())).async().execute();
            SQLite.delete(FavoriteEntity.class).where(FavoriteEntity_Table.favorite_list.eq(favoriteEntityList.get(position).getList())).async().execute();
            favoriteEntityList.remove(position);
        }
    }

    class FavoriteFragmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView name;
        View selectedItem;
        private ClickListener clickListener;
        public FavoriteFragmentHolder(View item, ClickListener clickListener){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item_favorite);
            selectedItem = itemView.findViewById(R.id.selected_overlay);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null) clickListener.onItemSelected(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return clickListener != null && clickListener.onItemLongSelected(getAdapterPosition());
        }
    }
    public void removeItems(List<Integer> positions) {
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                for (int i = 0; i < positions.size();i ++){
                    removeItem(positions.get(0));
                    positions.remove(0);
                    SQLite.update(MainEntity.class).set(MainEntity_Table.favorite.eq(false)).where(MainEntity_Table.content.eq(favoriteEntityList.get(positions.get(0)).getList())).async().execute();
                    SQLite.delete(FavoriteEntity.class).where(FavoriteEntity_Table.favorite_list.eq(favoriteEntityList.get(positions.get(0)).getList())).async().execute();
                }

            }
        }
    }
    public String selectItem(List<Integer> positions) {
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                return String.valueOf((favoriteEntityList.get(positions.get(0)).getList()));
            }
        }
        return null;
    }


}
