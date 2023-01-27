package it.unipi.BGnet.controllers.api;

import com.google.gson.Gson;

import it.unipi.BGnet.DTO.UserDTO;
import it.unipi.BGnet.service.user.UserService;
import it.unipi.BGnet.Utilities.SessionVariables;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@SessionAttributes("sessionVariables")
public class LoadProfileController {
    @Autowired
    UserService userService;
    @GetMapping("api/loadPersonalProfile")
    public @ResponseBody String personalProfile(Model model) {
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null)
            return new Gson().toJson(false);
        if(sv.myself == null)
            return new Gson().toJson(false);
        return new Gson().toJson(userService.loadProfile(sv.myself, null));
    }
    @GetMapping("api/loadProfile")
    public @ResponseBody String profile(Model model, @RequestParam(value = "username") String username) {
        UserDTO answer;
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null)
            answer = userService.loadProfile(username, null);
        else if(sv.myself == null)
            answer = userService.loadProfile(username, null);
        else
            answer = userService.loadProfile(username, sv.myself);
        if(answer == null)
            return new Gson().toJson(false);
        answer.setMyself(username.equals(sv.myself));
        return new Gson().toJson(answer);
    }

    @GetMapping("api/followUser")
    public @ResponseBody boolean followUser(Model model, @RequestParam(value = "user")String username){
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        return userService.followUser(sv.myself, username);
    }
    @GetMapping("api/unfollowUser")
    public @ResponseBody boolean unfollowUser(Model model, @RequestParam(value = "user")String username){
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        return userService.unfollowUser(sv.myself, username);
    }
}
