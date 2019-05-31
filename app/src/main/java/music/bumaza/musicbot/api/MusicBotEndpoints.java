package music.bumaza.musicbot.api;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MusicBotEndpoints {



    @POST("identify/song")
    Call<ApiResponse> sendToIdentifySong(@Body ApiRequestBody<ApiSongRequestBody> request);

}
