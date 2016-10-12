package list.umorili.android.com.umorili.entity;


import com.orm.dsl.Table;

@Table(name = "list_main")
public class MainEntity {

    private long id;
    String name;
    boolean favorite;

    public MainEntity(){}

    public MainEntity(String name, boolean favorite){
        this.name = name;
        this.favorite = favorite;
    }

    public long getId() {
        return id;
    }
}
