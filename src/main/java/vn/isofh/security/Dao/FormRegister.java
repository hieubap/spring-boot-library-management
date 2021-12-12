package vn.isofh.security.Dao;

public class FormRegister extends FormEditUser {
    protected String username;
    protected String password;

    public FormRegister() {
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
