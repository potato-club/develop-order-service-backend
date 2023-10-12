package com.server.dos.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String provider;

    @Column(name = "token")
    private String token;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Builder
    public User (String name, String email,String picture,Role role,String provider){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
    }

    public User update(String name,String picture) {
        this.name = name;
        this.picture = picture;
        
        return this;
    }
    public String getRoleKey(){
        return this.role.getKey();
    }

    public void setToken(String token){
        this.token = token;
    }
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
