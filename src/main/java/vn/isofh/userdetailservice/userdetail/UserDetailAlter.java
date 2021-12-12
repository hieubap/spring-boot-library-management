package vn.isofh.userdetailservice.userdetail;

import vn.isofh.userdetailservice.model.Account;
import vn.isofh.userdetailservice.model.Authority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailAlter implements UserDetails {
    private final String username;
    private final String password;
    private Collection<Authority> authorities;

    public UserDetailAlter(Account user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        if (user.getRoleInformation() != null) {
            this.authorities = user.getRoleInformation().getAuthorities();
            authorities.add(new Authority("ROLE_" + user.getRoleInformation().getName()));
        }
        else{
            authorities = new ArrayList<>();
            authorities.add(new Authority("ROLE_USER"));
        }
    }

    @Override
    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
