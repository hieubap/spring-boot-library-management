package vn.isofh.exception.exception;

public class ResourceException extends RuntimeException{
    private int status;

    public ResourceException(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
