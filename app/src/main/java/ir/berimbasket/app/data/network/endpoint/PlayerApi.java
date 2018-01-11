package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Player;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access players and manipulate them if needed
 */

public interface PlayerApi {

    @GET("getPlayers.php")
    Call<List<Player>> getPlayers(@Query("id") int id, @Query("pusheid") String pusheId, @Query("username") String username);
}