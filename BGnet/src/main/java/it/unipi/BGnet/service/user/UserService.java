package it.unipi.BGnet.service.user;

import it.unipi.BGnet.DTO.GameDTO;
import it.unipi.BGnet.model.Post;
import it.unipi.BGnet.model.User;
import it.unipi.BGnet.DTO.PostDTO;
import it.unipi.BGnet.DTO.UserDTO;
import it.unipi.BGnet.repository.GameRepository;
import it.unipi.BGnet.repository.UserRepository;

import org.neo4j.driver.Record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service("mainUserService")
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepo;

    @Autowired
    GameRepository gameRepo;
    public boolean addUser(User user) {
        if(!userRepo.addUser(user))
            return false;

        return userRepo.createNewUserNeo4j(user.getUsername());
    }
    public UserDTO getUserByEmail(String email) {
        Optional<User> result = userRepo.getUserByEmail(email);
        if(result.isEmpty())
            return null;
        return new UserDTO(result.get().getUsername(), result.get().getPassword());
    }
    public UserDTO getUser(String username) {
        Optional<User> result = userRepo.getUserByUsername(username);
        if(result.isEmpty())
            return null;
        return new UserDTO(result.get().getUsername(), result.get().getPassword());
    }
    public UserDTO loadProfile(String username) {
        Optional<User> result = userRepo.getUserByUsername(username);
        if(result.isEmpty())
            return null;
        UserDTO profile = new UserDTO(result.get().getUsername(), result.get().getFirstName(), result.get().getLastName(), result.get().getImg());
        profile.setMostRecentPosts(result.get().getMostRecentPosts());
        return profile;
    }
    public List<GameDTO> getSuggestedGames(String username){
        List<GameDTO> suggestedGames = userRepo.getSuggestedGames(username);
        logger.warn("SUGGESTED GAMES" + suggestedGames.size());
        if(suggestedGames.size() < 4){
            List<GameDTO> famousGames = gameRepo.getFamousGames();
            while(suggestedGames.size() < 4){
                for(GameDTO fam: famousGames){
                    boolean found = false;
                    for(GameDTO sugg: suggestedGames){
                        if(sugg.getName().equals(fam.getName())){
                            found=true;
                            break;
                        }
                    }
                    if(!found){
                        suggestedGames.add(fam);
                    }
                }
            }
        }
        return suggestedGames;
    }
    public List<GameDTO> getMostFamousGames() {
        return gameRepo.getFamousGames();
    }
    public List<UserDTO> getSuggestedUsers(String username){
        List<UserDTO> suggestedUsers = userRepo.getSuggestedUser(username);
        if(suggestedUsers.size()<4){
            List<UserDTO> famousUsers = userRepo.getFamousUsers();
            while(suggestedUsers.size()<4){
                for(UserDTO fam: famousUsers){
                    boolean found=false;
                    for(UserDTO sugg: suggestedUsers){
                        if(sugg.getUsername().equals(fam.getUsername())){
                            found=true;
                            break;
                        }
                    }
                    if(!found){
                        suggestedUsers.add(fam);
                    }
                }
            }
        }
        return suggestedUsers;
    }
    public List<UserDTO> getMostFamousUsers() {
        return userRepo.getFamousUsers();
    }
    public boolean isAdmin(String username){
        return userRepo.checkAdmin(username);
    }
}
