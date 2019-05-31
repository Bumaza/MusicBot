package music.bumaza.musicbot.api;

public class ApiRequestBody<T> {

    public ApiRequestBodyHeader header = new ApiRequestBodyHeader();
    public T data;

    public class ApiRequestBodyHeader{
        public String token;
    }

    public ApiRequestBody(T data) {
        this.data = data;
    }

    public static class Empty extends ApiRequestBody<Object>{
        public Empty() {
            super(new Object());
        }
    }
    public static Empty getEmpty(){
        return new Empty();
    }
}
