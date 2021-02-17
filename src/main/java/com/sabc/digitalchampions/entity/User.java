package com.sabc.digitalchampions.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, updatable = false)
    private String ref;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String role;

    public User() {
        this.createdAt = new Date();
        this.lastUpdatedAt = new Date();
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getRef() {
        return ref;
    }

    public User setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public User setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ref.equals(user.ref) &&
                username.equals(user.username) &&
                Objects.equals(createdAt, user.createdAt) &&
                role.equals(user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref, createdAt, username, role); //
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", role='" + role + '\'' +
                '}';
    }

    @JsonIgnore
    public boolean isCorrect() {
        return (ref != null && !ref.isEmpty()) &&
                (password != null && !password.isEmpty()) &&
                (role != null && !role.isEmpty());
    }
}
