package list.umorili.android.com.umorili.entity;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import list.umorili.android.com.umorili.database.AppDatabase;
import list.umorili.android.com.umorili.rest.models.GetListModel;

@Table(database = AppDatabase.class,insertConflict = ConflictAction.REPLACE)
public class MainEntity extends BaseModel{

    @PrimaryKey()
    private String id;

    @Column(name = "content")
    private String list;

    @Column(name = "favorite")
    private boolean favorite;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public static List<MainEntity> listUmor(){
        return SQLite.select()
                .from(MainEntity.class)
                .orderBy(MainEntity_Table.id, false)
                .queryList();
    }
    public static List<MainEntity> listUmorFavorite(){
        return SQLite.select(MainEntity_Table.content)
                .from(MainEntity.class)
                .where(MainEntity_Table.favorite.eq(true))
                .queryList();
    }
    public  static void updateFavorite(@NonNull String mainId, boolean b){
        SQLite.update(MainEntity.class)
                .set(MainEntity_Table.favorite.eq(b))
                .where(MainEntity_Table.id.eq(mainId))
                .queryList();
    }



    public static void deleteAll(){
        SQLite.delete().from(MainEntity.class).async().execute();
    }


}
