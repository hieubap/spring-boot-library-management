package library.security.enums;

public enum EAuthority {
    USER_READ("BOOK_READ"),
    USER_WRITE("BOOK_WRITE"),
    ADMIN_ROLE("ADMIN");

    public final String permission;

    EAuthority(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
