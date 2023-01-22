package it.unipi.BGnet.DTO;

import it.unipi.BGnet.model.Post;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    // private int yearRegistered;
    // private String email;
    // private String stateOfProvince;
    // private String country;
    // private String continent;
    private String img;
    private List<PostDTO> mostRecentPosts;

    private List<InCommonGenericDTO> inCommonFollowers;

    private List<TournamentDTO> inCommonTournaments;
    public UserDTO(String username, String firstName, String lastName, String img) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.img = img;
    }

    public List<InCommonGenericDTO> getInCommonFollowers() {
        return inCommonFollowers;
    }

    public void setInCommonFollowers(List<InCommonGenericDTO> inCommonFollowers) {
        this.inCommonFollowers = inCommonFollowers;
    }

    public List<TournamentDTO> getInCommonTournaments() {
        return inCommonTournaments;
    }

    public void setInCommonTournaments(List<TournamentDTO> inCommonTournaments) {
        this.inCommonTournaments = inCommonTournaments;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<PostDTO> getMostRecentPosts() {
        return mostRecentPosts;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setMostRecentPosts(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setGame(post.getGame());
            postDTO.setAuthor(post.getAuthor());
            postDTO.setLikes(post.getLikes().size());
            postDTO.setComments(post.getComments().size());
            postDTO.setDate(post.getTimestamp());
            postDTO.setText(post.getText());
            postDTOList.add(postDTO);
        }
        this.mostRecentPosts =  postDTOList;
    }
}
