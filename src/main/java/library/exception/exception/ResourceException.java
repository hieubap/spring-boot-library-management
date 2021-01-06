package library.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceException extends RuntimeException{
    private int status;

    public ResourceException(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
