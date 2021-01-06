package library.exception.exception;

public class ApiRequestException extends RuntimeException{
    public String data;
    public ApiRequestException(String message){
        super(message);
    }

    public ApiRequestException(String message, String data) {
        super(message);
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
