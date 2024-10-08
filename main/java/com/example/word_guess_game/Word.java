package com.example.word_guess_game;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wid;
    private String wordName;
    private String hints;
    private String image;
    // Getters and Setters
    private String level;
    public Long getWid() {
        return wid;
    }
    public void setWid(Long wid) {
        this.wid = wid;
    }
    public String getWordName() {
        return wordName;
    }
    public void setWordName(String wordName) {
        this.wordName = wordName;
    }
    public String getHints() {
        return hints;
    }
    public void setHints(String hints) {
        this.hints = hints;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}