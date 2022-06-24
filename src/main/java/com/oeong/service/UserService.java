package com.oeong.service;

import com.oeong.entity.User;

public interface UserService {
    User login(User user);
    void register(User user);
}
