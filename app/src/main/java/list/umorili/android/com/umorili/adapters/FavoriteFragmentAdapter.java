package list.umorili.android.com.umorili.adapters;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.FavoriteEntity;
import list.umorili.android.com.umorili.entity.MainEntity;


public class FavoriteFragmentAdapter extends RecyclerView.Adapter<FavoriteFragmentAdapter.FavoriteFragmentHolder>{
    FavoriteEntity favoriteEntity;
    List<FavoriteEntity> favoriteEntityList;
    public FavoriteFragmentAdapter(List<FavoriteEntity> favoriteEntity) {
        this.favoriteEntityList = favoriteEntity;
    }

    @Override
    public FavoriteFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_fragment_item, parent, false);
        return new FavoriteFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteFragmentHolder holder, int position) {
        favoriteEntity = favoriteEntityList.get(position);
        holder.name.setText(favoriteEntity.getList());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteEntityList.size();
    }

    class FavoriteFragmentHolder extends RecyclerView.ViewHolder{

        TextView name;
        public FavoriteFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item_favorite);
        }

    }
}
