package list.umorili.android.com.umorili.models;

import android.app.Application;


public class MainModel extends Application{
    private String name;

    public MainModel (String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
