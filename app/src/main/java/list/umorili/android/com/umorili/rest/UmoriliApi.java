package list.umorili.android.com.umorili.rest;

import list.umorili.android.com.umorili.GetListModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 12.10.2016.
 */

public interface UmoriliApi {
    @GET("get")
    Call<GetListModel> getListModel (@Query("site") String site,
                                        @Query("name") String name,
                                        @Query("desc") String desc,
                                        @Query("link") String link,
                                        @Query("elementPureHtml") String elementPureHtml);


}