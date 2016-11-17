package list.umorili.android.com.umorili.rest;

import android.support.annotation.NonNull;
import java.io.IOException;
import java.util.List;
import list.umorili.android.com.umorili.rest.models.GetListModel;

public final class RestService {

    private RestClient restClient;
    public RestService(){
        restClient = new RestClient();
    }

    public List<GetListModel> viewListInMainFragmenr (@NonNull String site,
                                                @NonNull String name,
                                                @NonNull int num) throws IOException{

        return  restClient.getUmoriliApi()
                .getListModel(site, name, num)
                .execute().body();

    }

}
