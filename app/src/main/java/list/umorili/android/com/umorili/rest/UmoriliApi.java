package list.umorili.android.com.umorili.rest;

import java.util.List;

import list.umorili.android.com.umorili.rest.models.GetListModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface UmoriliApi {
    @GET("api/get")
    Call<List<GetListModel>> getListModel (@Query("site") String site,
                                           @Query("name") String name,
                                           @Query("num") int num);
}
