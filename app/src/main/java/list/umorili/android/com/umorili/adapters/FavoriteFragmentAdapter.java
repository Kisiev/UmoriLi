package list.umorili.android.com.umorili.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import list.umorili.android.com.umorili.R;
import list.umorili.android.com.umorili.entity.MainEntity;

/**
 * Created by User on 12.10.2016.
 */

public class FavoriteFragmentAdapter extends RecyclerView.Adapter<FavoriteFragmentAdapter.FavoriteFragmentHolder>{

    List<MainEntity> mainEntityList;
    public FavoriteFragmentAdapter(List<MainEntity> mainEntity) {
        this.mainEntityList = mainEntity;
    }

    @Override
    public FavoriteFragmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_fragment_item, parent, false);
        return new FavoriteFragmentHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteFragmentHolder holder, int position) {
        MainEntity mainEntity = mainEntityList.get(position);
        holder.name.setText(mainEntity.getList());
    }

    @Override
    public int getItemCount() {
        return mainEntityList.size();
    }

    class FavoriteFragmentHolder extends RecyclerView.ViewHolder{

        TextView name;
        public FavoriteFragmentHolder(View item){
            super(item);
            name = (TextView) item.findViewById(R.id.name_item_favorite);
        }

    }
}
