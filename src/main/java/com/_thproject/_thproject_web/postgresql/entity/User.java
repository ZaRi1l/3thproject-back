package com._thproject._thproject_web.postgresql.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usertab")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "userid", nullable = false, unique = true, length = 100)
    private String userid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false, length = 20)
    private String userRole;

    @Builder
    public User(String userid, String username, String password, String userRole) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.userRole));
    }

    @Override
    public String getUsername() {
        return this.userid; // Spring Security에서는 Username을 ID로 사용
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getRealUsername() {
        return this.username;
    }


    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
