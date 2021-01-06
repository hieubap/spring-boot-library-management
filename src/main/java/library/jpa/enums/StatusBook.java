package library.jpa.enums;

public enum StatusBook {
    NORMAL("normal"),
    MISSING("miss"),
    ORDERED("ordered"),
    BORROWED("borrowed");

    private final String status;

    StatusBook(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}