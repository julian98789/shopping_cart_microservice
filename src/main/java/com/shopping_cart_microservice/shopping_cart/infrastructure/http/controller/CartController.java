package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.application.handler.card_handler.ICartHandler;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartHandler cartHandler;

    @Operation(summary = "Agregar artículo al carrito", description = "Agrega un artículo al carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artículo agregado al carrito", content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PreAuthorize(Util.ROLE_CLIENT )
    @PostMapping("/add-article-to-card")
    public ResponseEntity<CartResponse> addArticleToCart( @Valid @RequestBody CartRequest cartRequest) {

        CartResponse cartResponse = cartHandler.addArticleToCart(cartRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @Operation(summary = "Eliminar artículo del carrito", description = "Elimina un artículo del carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo eliminado del carrito", content = @Content),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado", content = @Content)
    })
    @PreAuthorize(Util.ROLE_CLIENT )
    @DeleteMapping("/delete-article-from-cart/{articleId}")
    public ResponseEntity<String> removeArticleFromCart(@PathVariable Long articleId) {
        cartHandler.removeProductToCart(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(Util.ARTICLE_DELETED_SUCCESSFULLY);
    }

    @Operation(summary = "Obtener artículos del carrito", description = "Obtiene una lista paginada de artículos en el carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de artículos en el carrito", content = @Content(schema = @Schema(implementation = Paginated.class)))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("/article-cart")
    public ResponseEntity<Paginated<ArticleDetailsCartResponse>> getAllArticlesPaginatedByIds(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "name") @NotBlank @Size(min = 1) String sort,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String brandName) {

        Paginated<ArticleDetailsCartResponse> articles = cartHandler.getAllArticlesPaginatedByIds(
                page, size, sort, ascending, categoryName, brandName);

        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @Operation(summary = "Obtener carrito por usuario", description = "Obtiene el carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito del usuario", content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("get-cart-by-user")
    public ResponseEntity<List<CartResponse>> buyProducts(){

        List<CartResponse> cartByUserId = cartHandler.findCartByUserId();

        return ResponseEntity.status(HttpStatus.OK).body(cartByUserId);
    }

    @Operation(summary = "Eliminar carrito", description = "Elimina el carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito eliminado", content = @Content)
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @DeleteMapping("/delete-cart")
    public ResponseEntity<String> deleteCart(){
        cartHandler.deleteCart();
        return ResponseEntity.status(HttpStatus.OK).body(Util.DELETE_CART_RESPONSE_BODY);
    }
    @Operation(summary = "Obtener última fecha de actualización del carrito", description = "Obtiene la última fecha de actualización del carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Última fecha de actualización del carrito", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @GetMapping("/latest-update")
    public ResponseEntity<String> getLatestCartUpdateDate() {
        String latestUpdateDate = cartHandler.getLatestCartUpdateDate();
        return ResponseEntity.ok(latestUpdateDate);
    }

    @Operation(summary = "Actualizar cantidad de producto en el carrito", description = "Actualiza la cantidad de un producto en el carrito del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cantidad de producto actualizada", content = @Content(schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    @PreAuthorize(Util.ROLE_CLIENT)
    @PatchMapping("/update-quantity")
    public ResponseEntity<CartResponse> updateProductQuantity(@RequestBody CartUpdateQuantityRequest cartRequest) {

        CartResponse cartResponse = cartHandler.updateCartQuantity(cartRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

}