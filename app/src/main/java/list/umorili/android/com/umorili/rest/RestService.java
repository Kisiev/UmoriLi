package list.umorili.android.com.umorili.rest;

import android.support.annotation.NonNull;

import java.io.IOException;

import list.umorili.android.com.umorili.rest.models.GetListModel;

/**
 * Created by User on 15.10.2016.
 */

public final class RestService {

    private RestList restList;

    public RestService(){
        restList = new RestList();
    }

    public GetListModel viewListInMainFragmenr (@NonNull String site,
                                                @NonNull String name,
                                                @NonNull String num) throws IOException{

        return restList.getUmoriliApi()
                .getListModel(site, name, num)
                .execute().body();

    }
}
