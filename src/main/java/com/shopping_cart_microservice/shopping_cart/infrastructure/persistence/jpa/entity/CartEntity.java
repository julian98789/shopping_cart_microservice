package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cart", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "article_id", nullable = false)
        private Long articleId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "quantity", nullable = false)
        private Integer quantity;

        @Column(name = "creation_date", nullable = false)
        private LocalDate creationDate;

        @Column(name = "last_updated_date", nullable = false)
        private LocalDate lastUpdatedDate;
}

