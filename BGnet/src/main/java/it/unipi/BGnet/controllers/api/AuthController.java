package it.unipi.BGnet.controllers.api;

import it.unipi.BGnet.model.User;
import it.unipi.BGnet.service.user.UserService;

import com.google.gson.Gson;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;



@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/api/login")
    public String login(Model model, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Gson gson = new Gson();
        User user = userService.getUser(username);
        if(user == null)
        {
            return gson.toJson("{type: 1, message : Incorrect username}");
        }
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
        if(!pbkdf2PasswordEncoder.matches(password, user.getPassword()))
        {
            return gson.toJson("{type: 2, message : Incorrect password}");
        }
        return gson.toJson("{type: 0, message : OK}");
    }
}