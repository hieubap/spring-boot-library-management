package vn.isofh.exception.exception;

public class ExistNullOrBorrowedBookException extends RuntimeException{
    private Object data;

    public ExistNullOrBorrowedBookException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public Object getData(){
        return data;
    }
}
