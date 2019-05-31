package music.bumaza.musicbot.api;

import retrofit2.Callback;

public class ApiRequest extends MusicBotApi{

    public static void sendToIdentifySong(ApiSongRequestBody body, Callback<ApiResponse> callback){
        initApi();
        endpoints.sendToIdentifySong(new ApiRequestBody<ApiSongRequestBody>(body)).enqueue(callback);
    }
}
