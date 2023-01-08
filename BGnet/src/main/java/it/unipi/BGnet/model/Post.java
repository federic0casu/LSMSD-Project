package it.unipi.BGnet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "post")
public class Post {

    @Id
    private String id;
    private String author;
    private String game;
    private String text;
    private List<String> likes;
    private OffsetDateTime dateTime;
    private List<Comment> commentList;

    public Post(String id, String author, String game, String text) {
        this.id = id;
        this.author = author;
        this.game = game;
        this.text = text;
    }

    public Post(String id, String author, String game, String text, OffsetDateTime dateTime, List<Comment> commentList) {
        this.id = id;
        this.author = author;
        this.game = game;
        this.text = text;
        this.dateTime = dateTime;
        this.commentList = commentList;
    }

    public void addLike(String name){
        likes.add(name);
    }

    public void removeLike(String name){
        likes.remove(name);
    }

    public void addComment(Comment comment){
        commentList.add(0, comment);
    }

    public void removeComment(Comment comment){
        commentList.remove(comment);
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}