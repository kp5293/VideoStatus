package com.skylix.videostatus.ModelClass;

/**
 * Created by Destiny on 18-Jul-17.
 */

public class CategoryModel {

    Integer id;
    Integer cat_id;

    public Integer getFav_id() {
        return fav_id;
    }

    public void setFav_id(Integer fav_id) {
        this.fav_id = fav_id;
    }

    Integer fav_id;
    private String date;
    private String cat_type;
    private String cat_alias;
    private String language;
    private String url;
    private String approved;
    private String title;
    private String fav_counter;
    private String dislikes;
    private String likes;
    private String thumnail;
    private String email;

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFav_counter() {
        return fav_counter;
    }

    public void setFav_counter(String fav_counter) {
        this.fav_counter = fav_counter;
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getCat_alias() {
        return cat_alias;
    }

    public void setCat_alias(String cat_alias) {
        this.cat_alias = cat_alias;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CategoryModel(Integer id, String title,String cat_type, String cat_alias, String url, String fav_counter, String dislikes, String likes) {
        this.id = id;
        this.title = title;
        this.cat_type = cat_type;
        this.cat_alias = cat_alias;
        this.url = url;
        this.fav_counter = fav_counter;
        this.dislikes = dislikes;
        this.likes = likes;
    }

    public CategoryModel(Integer id) {
        this.id = id;
    }

    public CategoryModel() {
    }
}
