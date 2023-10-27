package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuProductTest {

    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void setUp() {
        product = Product.create("후라이드", BigDecimal.valueOf(16000));
        menuGroup = MenuGroup.create("두마리메뉴");
        menu = Menu.create("두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), menuGroup);
    }

    @DisplayName("메뉴 상품 생성 시, 수량이 0개 이하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -0L})
    void menuProduct_FailWithInvalidQuantity(Long invalidQuantity) {
        // when & then
        assertThatThrownBy(() -> MenuProduct.create(menu, product, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품의 수량은 1개 이상이어야 합니다.");
    }

    @DisplayName("메뉴 상품을 생성할 수 있다.")
    @Test
    void menuProduct() {
        // then
        assertDoesNotThrow(() -> MenuProduct.create(menu, product, 1L));
    }

    @DisplayName("상품 가격과 수량을 곱한 가격을 반환한다.")
    @Test
    void calculateMenuProductPrice() {
        // given
        MenuProduct menuProduct = MenuProduct.create(menu, product, 1L);
        double expected = product.getPrice().doubleValue() * menuProduct.getQuantity();

        // when
        double price = menuProduct.calculateMenuProductPrice();

        // then
        assertThat(price).isEqualTo(expected);
    }
}
