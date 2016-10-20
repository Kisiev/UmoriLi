package list.umorili.android.com.umorili.entity;

import android.database.Cursor;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.EBean;

import java.util.List;

import list.umorili.android.com.umorili.database.AppDatabase;

@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE)
public class FavoriteEntity extends BaseModel{

    @PrimaryKey()
    private String id;

    @Column(name = "id_list")
    private String id_list;

    @Column(name = "favorite_list")
    private String list;

    public String getId() {
        return id;
    }

    public String getId_list() {
        return id_list;
    }

    public void setId_list(String id_list) {
        this.id_list = id_list;
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



    public static void insert (String id, String value){
            SQLite.insert(FavoriteEntity.class)
                    .columns("id_list", "favorite_list")
                    .values(id, value)
                    .execute();

    }

    public static List<FavoriteEntity> selectedALL(){

        return SQLite.select()
                .from(FavoriteEntity.class)
                .queryList();

    }

    public static void deleteAll(String id){
        SQLite.delete()
                .from(FavoriteEntity.class)
                .where(FavoriteEntity_Table.id_list.eq(id))
                .async()
                .execute();
    }


}
