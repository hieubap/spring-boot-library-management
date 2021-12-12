package vn.isofh.security.enums;

import com.google.common.collect.Sets;

import java.util.Set;

import static vn.isofh.security.enums.EAuthority.*;

public enum ERole {
    USER(Sets.newHashSet(USER_READ,USER_WRITE)),
    ADMIN(Sets.newHashSet(USER_READ,USER_WRITE,ADMIN_ROLE));

    public final Set<EAuthority> permissions;

    ERole(Set<EAuthority> permissions) {
        this.permissions = permissions;
    }

    public Set<EAuthority> getPermissions() {
        return permissions;
    }
}
