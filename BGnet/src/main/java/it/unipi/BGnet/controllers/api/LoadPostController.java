package it.unipi.BGnet.controllers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unipi.BGnet.DTO.PostDTO;
import it.unipi.BGnet.Utilities.SessionVariables;
import it.unipi.BGnet.service.pages.PostService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@SessionAttributes("sessionVariables")
public class LoadPostController {
    @Autowired
    PostService postService;
    @RequestMapping("/api/addPost")
    public boolean savePost(Model model, @RequestParam("game") String game, @RequestParam("text")String text) {
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null)
            return false;
        if(sv.myself == null)
            return false;
        String author = sv.myself;
        return postService.addPost(game, author, text);
    }
    @GetMapping("/api/getPost")
    public @ResponseBody String getPostList(Model model, @RequestParam(value = "page") int pageNumber) {
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null)
            return null;
        if(sv.gameToDisplay == null)
            return null;
        List<PostDTO> postList = postService.loadPostPage(model, sv.gameToDisplay, pageNumber);
        sv.currentPage = pageNumber;
        model.addAttribute("sessionVariables", sv);
        Gson gson = new Gson();
        String result = gson.toJson(postList);
        return result;
    }
    @GetMapping("api/likePost")
    public @ResponseBody String likePost(Model model, @RequestParam(value = "post") String post, @RequestParam(value = "game") String game) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null || sv.myself == null) {
            return gson.toJson(0);
        }
        return gson.toJson(postService.likeUnlikePost(post, sv.myself, game));
    }
    @GetMapping("/api/getPages")
    public @ResponseBody String getHowManyPages(Model model) {
        SessionVariables sv = (SessionVariables) model.getAttribute("sessionVariables");
        if(sv == null)
            return null;
        if(sv.gameToDisplay == null)
            return null;
        int pages = postService.loadNumberOfPages(sv.gameToDisplay);
        Gson gson = new Gson();
        String result = gson.toJson(pages);
        return result;
    }
}
