package com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign;

import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleCartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = Util.STOCK_SERVICE_NAME, url = Util.STOCK_SERVICE_URL)
public interface IStockFeignClient {

    @GetMapping("/api/article/{articleId}")
    boolean articleExistsById(@PathVariable Long articleId);

    @GetMapping("/api/category/names-by-article/{articleId}")
    List<String> getCategoryNamesByArticleId(@PathVariable Long articleId);

    @GetMapping("/api/article/article-cart")
    Paginated<ArticleDetailsCartResponse>getArticlesCart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String brandName,
            @RequestBody ArticleCartRequest articleCartRequest);

    @GetMapping("/api/article/get-all-articles")
    List<ArticleDetailsCartModel> getAllArticles();

    @GetMapping("/api/article/{articleId}/check-quantity/{quantity}")
    boolean isStockSufficient(@PathVariable Long articleId, @PathVariable Integer quantity);


    @GetMapping("/api/article/{articleId}/price")
    Double getArticlePriceById(@PathVariable Long articleId);
}