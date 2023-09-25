package com.example.reader;

public class NewsItem{
    public String title;
    public String description;
    public String pubDate;
    public String link;
    public long id = -1;

    public boolean isFavorite;

    public void setTitle(String text) {
        this.title = text;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFavorite(boolean favorite) {this.isFavorite = favorite; }

    public void setId(long id) {this.id = id;}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public long getId() {
        return id;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
}