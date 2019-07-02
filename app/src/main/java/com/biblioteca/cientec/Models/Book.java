package com.biblioteca.cientec.Models;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String isbn;
    private String title;
    private String originalTile;
    private int edition;
    private String publisher;
    private String coverUrl;
    private String coverName;
    private String description;
    private String authorId;
    private int numberOfPages;
    private String language;

    public Book() {
        this.id = -1;
        this.isbn = "";
        this.title = "";
        this.originalTile = "";
        this.edition = -1;
        this.publisher = "";
        this.coverUrl = "";
        this.coverName = "";
        this.description = "";
        this.authorId = "";
        this.numberOfPages = -1;
        this.language = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTile() {
        return originalTile;
    }

    public void setOriginalTile(String originalTile) {
        this.originalTile = originalTile;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
