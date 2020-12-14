package library.security.DAO;

public class UsernameAndPasswordDAO {
    private String username;
    private String password;

    public UsernameAndPasswordDAO(){
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