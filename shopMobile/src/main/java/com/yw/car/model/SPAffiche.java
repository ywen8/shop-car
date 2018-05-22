package com.yw.car.model;

import java.io.Serializable;

/**
 * Created by yw on 2018/3/9.
 */

public class SPAffiche implements SPModel, Serializable {
    private String article_id;
    private String title;
    private String link;

    public String getArticle_id() {
        return article_id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
                "article_id", "article_id",
                "title", "title",
                "link", "link"
        };
    }
}
