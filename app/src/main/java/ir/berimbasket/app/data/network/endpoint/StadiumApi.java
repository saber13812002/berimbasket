package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Stadium;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access stadium
 */

public interface StadiumApi {

    @GET("get.php")
    Call<List<Stadium>> getStadium(@Query("id") int id, @Query("pusheid") String pusheId,
                                   @Query("username") String username);
}
