package list.umorili.android.com.umorili.rest;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import list.umorili.android.com.umorili.rest.models.GetListModel;

/**
 * Created by User on 15.10.2016.
 */

public final class RestService {

    private RestList restList;

    public RestService(){
        restList = new RestList();
    }

    public List<GetListModel> viewListInMainFragmenr (@NonNull String site,
                                                @NonNull String name,
                                                @NonNull int num) throws IOException{

        return  restList.getUmoriliApi()
                .getListModel(site, name, num)
                .execute().body();

    }


}
