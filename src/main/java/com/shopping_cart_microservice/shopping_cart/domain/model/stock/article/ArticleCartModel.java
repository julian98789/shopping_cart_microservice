package com.shopping_cart_microservice.shopping_cart.domain.model.stock.article;

import java.util.List;

public class ArticleCartModel {
    private List<Long> articleIds;

    public ArticleCartModel(List<Long> articleIds) {
        this.articleIds = articleIds;
    }

    public List<Long> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }
}