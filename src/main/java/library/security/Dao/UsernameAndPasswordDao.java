package library.security.Dao;

public class UsernameAndPasswordDao {
    private String username;
    private String password;

    public UsernameAndPasswordDao(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}