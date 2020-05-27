package com.tommy.fsd.service;

import com.tommy.fsd.entities.Cart;
import com.tommy.fsd.entities.User;
import com.tommy.fsd.enums.ResultEnum;
import com.tommy.fsd.exception.MyException;
import com.tommy.fsd.repo.CartRepo;
import com.tommy.fsd.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DependsOn("passwordEncoder")
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CartRepo cartRepo;

    public User findOne(String email) {
        return userRepo.findByEmail(email);
    }

    public User findName(String name) {
        System.out.println(name);
        return userRepo.findByName(name);
    }

    @Transactional
    public User save(User user) {
        //register
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepo.save(user);

            // initial Cart
            Cart savedCart = cartRepo.save(new Cart(savedUser));
            //savedUser.setCart(savedCart);
            return userRepo.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }

    }

    @Transactional
    public User update(User user) {
        User oldUser = userRepo.findByEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepo.save(oldUser);
    }

}
