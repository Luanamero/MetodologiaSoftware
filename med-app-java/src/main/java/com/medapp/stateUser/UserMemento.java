package com.medapp.stateUser;

import com.medapp.models.User;

public class UserMemento {
    protected final String username;
    protected final String password;
    protected final String email;

    public UserMemento(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }   
}
