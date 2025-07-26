package com.karthik.projects.airBnbApp.services;

import com.karthik.projects.airBnbApp.entities.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User getUserByid(Long id);
}
