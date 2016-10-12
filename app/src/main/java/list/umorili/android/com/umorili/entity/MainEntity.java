package list.umorili.android.com.umorili.entity;


import com.orm.SugarRecord;

import java.util.List;

public class MainEntity extends SugarRecord{

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    String name;
    boolean favorite;

    public MainEntity(){}

    public MainEntity(String name, boolean favorite){
        this.name = name;
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "MainEntity{" +
                "name='" + name + '\'' +
                ", favorite=" + favorite +
                '}';
    }

    public static List<MainEntity> SelectAll (){
        List<MainEntity> mainEntities = MainEntity.listAll(MainEntity.class);
        return mainEntities;
    }
}
