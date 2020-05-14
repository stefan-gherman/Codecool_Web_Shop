package com.codecool.shop.dao;

import com.codecool.shop.model.User;

public interface UserDao {
    int add(User user);
    User getUserByEmail(String email);
    String getUserPasswordByEmail(String email);


}
