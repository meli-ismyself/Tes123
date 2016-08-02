package binaryworksindonesia.com.gpstracker.api;

import binaryworksindonesia.com.gpstracker.api.response.HajjPerson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Meli Oktavia on 5/10/2016.
 */
public interface ApiClient {
    @GET("/traccar/ws.php")
    Call<HajjPerson> getHajjPerson(@Query("app")String app,
                                   @Query("module")String module,
                                   @Query("action")String action,
                                   @Query("reqid")String reqid);
}
