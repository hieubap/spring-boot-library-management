package library.jpa.responceEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class EntityResponse<T> {
    private int status;
    @JsonIgnore
    private Timestamp timestamp;
    public String message;
    String time;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private T data;


    public EntityResponse(String message) {
        this.status = 200;
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        time = timestamp.toString();
    }

    public EntityResponse(int status, Timestamp time, String message, T data) {
        this.status = status;
        this.timestamp = time;
        this.message = message;
        this.data = data;
        this.time = timestamp.toString();
    }

    public EntityResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
        this.data = data;
        this.time = timestamp.toString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
