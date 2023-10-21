package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    private Product(final Long id, final String name, final BigDecimal price) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public static Product create(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    private void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명이 비어있습니다.");
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("상품 가격이 비어있습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격은 0원 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
