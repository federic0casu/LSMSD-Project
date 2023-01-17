package it.unipi.BGnet.repository;

import it.unipi.BGnet.model.Game;
import it.unipi.BGnet.model.Post;
import it.unipi.BGnet.repository.mongoDB.IGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GameRepository {
    @Autowired
    private IGameRepository gameMongo;

    public IGameRepository getMongo(){ return gameMongo; }

    // CRUD Methods
    // ----------------------------------------------------------------------------------------
    public boolean addGame(Game game){
        boolean result = true;
        try{
            gameMongo.save(game);
        } catch (Exception e){
            e.printStackTrace();
            result=false;
        }
        return result;
    }

    public boolean deleteGameById(String id) {
        boolean result = true;
        try {
            gameMongo.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean deleteGame(Game game) {
        boolean result = true;
        try {
            gameMongo.delete(game);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public Optional<Game> getGameById(String id){
        Optional<Game> game = Optional.empty();
        try {
            game = gameMongo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game;
    }

    public Optional<Game> getGameByName(String name){
        Optional<Game> game = Optional.empty();
        try {
            game = gameMongo.findByName(name);
            System.out.println(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game;
    }

    public List<Game> findAllGames() {
        List<Game> result = new ArrayList<>();
        try {
            result.addAll(gameMongo.findAll());
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean searchGame(String name) {
        boolean result = false;
        try {
            result = gameMongo.existsByNameRegex(name);
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int countGames(String name) {
        int result = 0;
        try {
            result = gameMongo.countByNameRegex(name);
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Game> searchGames(String pattern){
        List<Game> result = new ArrayList<>();
        try {
            result = gameMongo.findByNameRegex(pattern);
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public boolean addPost(String name, Post post){
        Optional<Game> game = getGameByName(name);
        if(game.isEmpty())
            return false;

        List<Post> list = game.get().getMostRecentPosts();
        list.add(0, post);
        list.remove(list.size() - 1);
        game.get().setMostRecentPosts(list);
        try{
            gameMongo.save(game.get());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removePost(String name, Post post){
        Optional<Game> game = getGameByName(name);
        if(game.isEmpty())
            return false;

        List<Post> list = game.get().getMostRecentPosts();
        list.add(0, post);
        list.remove(post);
        game.get().setMostRecentPosts(list);
        try{
            gameMongo.save(game.get());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updatePost(String name, Post olderPost, Post newPost){
        Optional<Game> game = getGameByName(name);
        if(game.isEmpty())
            return false;

        List<Post> list = game.get().getMostRecentPosts();
        if(!list.contains(olderPost))
            return true;

        list.set(list.indexOf(olderPost), newPost);
        game.get().setMostRecentPosts(list);
        try{
            gameMongo.save(game.get());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean existsById(String gameName) {
        return gameMongo.existsByName(gameName);
    }
}
