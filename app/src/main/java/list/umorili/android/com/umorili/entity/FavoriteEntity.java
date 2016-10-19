package list.umorili.android.com.umorili.entity;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import list.umorili.android.com.umorili.database.AppDatabase;

@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE)
public class FavoriteEntity extends BaseModel{

    @PrimaryKey()
    private String id;

    @Column(name = "favorite_list")
    private String list;
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



    public static void insert (String id){
        SQLite.insert(FavoriteEntity.class)
                .columns("favorite_list")
                .values(SQLite.select(MainEntity_Table.content)
                .from(MainEntity.class)
                .where(MainEntity_Table.id.eq(id)).querySingle())
                .execute();
    }

    public static List<FavoriteEntity> selectedALL(){

        return SQLite.select()
                .from(FavoriteEntity.class)
                .queryList();

    }

    public static void deleteAll(){
        SQLite.delete()
                .from(FavoriteEntity.class)
                .async()
                .execute();
    }


}
