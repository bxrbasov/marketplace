package com.spring.marketplace.handler.specification;

import com.spring.marketplace.database.model.Category;
import com.spring.marketplace.database.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class ProductSpecification {

    public static Specification<Product> byName(String name) {
        return ((root, query, cb) -> name == null?cb.conjunction():cb.like(root.get("name"), "%"+name+"%"));
    }

    public static Specification<Product> byCatygory(Category category) {
        return ((root, query, cb) -> category == null?cb.conjunction():cb.equal(root.get("category"), category));
    }

    public static Specification<Product> byPrice(BigDecimal price) {
        return ((root, query, cb) -> price == null?cb.conjunction():cb.lessThan(root.get("price"), price));
    }

    public static Specification<Product> byQuantity(BigInteger quantity) {
        return ((root, query, cb) -> quantity == null?cb.conjunction():cb.greaterThan(root.get("quantity"), quantity));
    }

}
