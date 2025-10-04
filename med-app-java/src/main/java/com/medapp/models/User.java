package com.medapp.models;

import java.io.Serializable;

import com.medapp.stateUser.UserCareTake;
import com.medapp.stateUser.UserMemento;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String username;
    protected String password;
    protected String email;
    protected UserMemento userMemento;
    UserCareTake userCareTake;

    public User() {
        // Construtor padrão necessário para Jackson
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        userCareTake = new UserCareTake();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getTipoUsuario();
    
    @Override
    public String toString() {
        return String.format("User{username='%s', email='%s', tipo='%s'}", 
                            username, email, getTipoUsuario());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username != null ? username.equals(user.username) : user.username == null;
    }
    
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    public void saveToMemento() {
        userCareTake.addMemento(new UserMemento(this));
    }

    public void restoreLastState() {
    UserMemento memento = userCareTake.getLastMemento();
    if (memento != null) {
        restoreFromMemento(memento);
    }
}

     public void restoreFromMemento(UserMemento memento) {
        this.username = memento.getUsername();
        this.password = memento.getPassword();
        this.email = memento.getEmail();
    }

}