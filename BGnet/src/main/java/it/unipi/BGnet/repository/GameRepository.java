package it.unipi.BGnet.repository;

import it.unipi.BGnet.DTO.GameDTO;
import it.unipi.BGnet.model.Game;
import it.unipi.BGnet.model.Post;
import it.unipi.BGnet.DTO.AnalyticDTO;
import it.unipi.BGnet.Utilities.Constants;
import it.unipi.BGnet.DTO.InCommonGenericDTO;
import it.unipi.BGnet.repository.neo4j.GameNeo4j;
import it.unipi.BGnet.repository.mongoDB.IGameRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import org.neo4j.driver.Record;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class GameRepository {
    @Autowired
    private IGameRepository gameMongo;
    @Autowired
    private MongoOperations mongoOperations;
    private final GameNeo4j gameNeo4j = new GameNeo4j();
    public IGameRepository getMongo(){ return gameMongo; }

    ///////////////////// MONGODB /////////////////////////
    public boolean addGameMongo(Game game){
        boolean result = true;
        try{
            gameMongo.save(game);
        } catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean deleteGameByIdMongo(String id) {
        boolean result = true;
        try {
            gameMongo.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean deleteGameMongo(Game game) {
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
        if(list.size() > Constants.RECENT_SIZE)
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
    public boolean updatePost(String name, Post olderPost, Post newPost) {
        Optional<Game> game = getGameByName(name);
        if(game.isEmpty())
            return false;
        List<Post> list = game.get().getMostRecentPosts();
        for(Post post: list)
            if(post.getId().equals(olderPost.getId())) {
                list.set(list.indexOf(post), newPost);
                break;
            }
        game.get().setMostRecentPosts(list);
        try {
            gameMongo.save(game.get());
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean existsById(String gameName) {
        return gameMongo.existsByName(gameName);
    }

    ///////////////////// NEO4J /////////////////////////
    public int getFollowersNumberByGamename(String gamename) {
        List<Record> followersNumber = gameNeo4j.findFollowerNumberByGamename(gamename);
        return (followersNumber.isEmpty()) ? 0 : followersNumber.get(0).get("numFollowers").asInt();
    }
    public boolean followGameByGamename(String username, String gamename) {
        return gameNeo4j.followGameByGamename(username, gamename);
    }
    public boolean unfollowGameByGamename(String username, String gamename) {
        return gameNeo4j.unfollowGameByGamename(username, gamename);
    }
    public List<InCommonGenericDTO> findInCommonFollowers(String username, String gamename) {
        List<InCommonGenericDTO> inCommonFollowers = new ArrayList<>();
        for(Record r: gameNeo4j.findInCommonFollowers(username, gamename)){
            String name = r.get("name").asString();
            String imgUrl = r.get("imgUrl").asString();
            InCommonGenericDTO inCommonGenericDTO = new InCommonGenericDTO(name, imgUrl);
            inCommonFollowers.add(inCommonGenericDTO);
        }
        return inCommonFollowers;
    }
    public boolean createNewGameNeo4j(String gamename){
        return gameNeo4j.createNewGame(gamename);
    }
    public boolean deleteGameNeo4j(String gamename){
        return gameNeo4j.deleteGame(gamename);
    }
    public boolean isFollowing(String username, String gamename) {
        List<Record> result = gameNeo4j.isFollowing(username, gamename);
        return !result.isEmpty() && result.get(0).get("isFollowing").asBoolean();
    }
    public List<GameDTO> getFamousGames(){
        List<GameDTO> famousGames = new ArrayList<>();
        for(Record r: gameNeo4j.getFamousGames()){
            GameDTO g = new GameDTO();
            g.setImage(r.get("imgUrl").asString());
            g.setName(r.get("game").asString());
            famousGames.add(g);
        }
        return famousGames;
    }
    public List<GameDTO> getRandomGames(){
        List<GameDTO> randomGames = new ArrayList<>();
        for(Record r: gameNeo4j.getRandomGames()){
            GameDTO g = new GameDTO();
            g.setImage(r.get("imgUrl").asString());
            g.setName(r.get("game").asString());
            randomGames.add(g);
        }
        return randomGames;
    }
    public List<Game> searchGamesFiltered(String pattern, String category) {
        List<Game> result = new ArrayList<>();
        try {
            result = gameMongo.findByNameRegexAndCategoriesContaining(pattern, category);
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public boolean addRate(String username, String gamename, int rate) {
        Optional<Game> game = getGameByName(gamename);
        if(game.isEmpty())
            return false;

        if(game.get().haveIVoted(username))
            return false;

        game.get().addRate(username, rate);
        try{
            gameMongo.save(game.get());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean removeRate(String username, String gamename) {
        Optional<Game> game = getGameByName(gamename);
        if(game.isEmpty())
            return false;

        if(!game.get().haveIVoted(username))
            return false;

        game.get().removeRate(username);

        try{
            gameMongo.save(game.get());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean checkRate(String username, String gamename) {
        Optional<Game> game = getGameByName(gamename);
        if(game.isEmpty())
            return false;

        return game.get().haveIVoted(username);
    }
    public List<AnalyticDTO> getMostPopularGames(){
        List<Record> list = gameNeo4j.getMostPopularGames();
        List<AnalyticDTO> listDTO = new ArrayList<>();
        int i=1;
        for(Record r:list){
            AnalyticDTO dto = new AnalyticDTO();
            dto.setField1(String.valueOf(i));
            dto.setField2(r.get("gamename").asString());
            dto.setField3(String.valueOf(r.get("popularity").asInt()));
            i++;
            listDTO.add(dto);
        }
        return listDTO;
    }
    public List<AnalyticDTO> findBestAndWorstGamesForCategory() {
        UnwindOperation unwindOperation = unwind("categories");
        ProjectionOperation selectAvgs = project().andExpression("name").as("name").
                andExpression("categories").as("category").andExpression("avg_rate").as("avg")
                .andExclude("_id");
        SortOperation sortOperation = sort(Sort.by(Sort.Direction.ASC, "avg"));
        GroupOperation groupOperation = group("$category")
                .last("$name").as("BestRatedGame")
                .last("$avg").as("BestRate")
                .first("$name").as("WorstRatedGame")
                .first("$avg").as("WorstRate");
        ProjectionOperation projectionOperation = project()
                .andExpression("_id").as("field1")
                .andExclude("_id")
                .andExpression("BestRatedGame").as("field3")
                .andExpression("WorstRatedGame").as("field2");
        Aggregation aggregation = newAggregation(unwindOperation, selectAvgs, sortOperation,
                groupOperation, projectionOperation);
        AggregationResults<AnalyticDTO> result = mongoOperations
                .aggregate(aggregation, "game", AnalyticDTO.class);

        return result.getMappedResults();
    }
}
