package com.medapp.stateUser;

import java.util.Stack;

public class UserCareTake {
    protected final Stack<UserMemento> mementos;

    public UserCareTake() {
        mementos = new Stack<UserMemento>();
    }

    public void addMemento(UserMemento memento) {
        mementos.push(memento);
    }

    public UserMemento getLastMemento() {
        if(mementos.size() <= 0) {
            return null;
        } else {
            UserMemento lastState = mementos.pop();
            return lastState;
        }
    }
}
