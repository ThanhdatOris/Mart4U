package com.ctut.mart4u.utils;


import com.ctut.mart4u.model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {
        // Constructor riêng để ngăn khởi tạo trực tiếp
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void clearSession() {
        currentUser = null;
    }
}