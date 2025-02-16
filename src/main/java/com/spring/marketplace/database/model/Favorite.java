package com.spring.marketplace.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setUser(User user) {
        this.user = user;
        user.getFavorites().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getFavorites().add(this);
    }

}
