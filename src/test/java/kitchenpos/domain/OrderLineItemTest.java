package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderLineItemTest {

    @DisplayName("주문 항목 생성 시, 수량이 0개 미만이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void orderLineItem_FailWithInvalidQuantity(Long invalidQuantity) {
        // given
        Order order = Order.create(OrderTable.create(0, false));

        // when & then
        assertThatThrownBy(() -> OrderLineItem.create(order, 1L, "두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목의 수량은 0개 이상이어야 합니다.");
    }

    @DisplayName("주문 항목을 생성할 수 있다.")
    @Test
    void orderLineItem() {
        // given
        Order order = Order.create(OrderTable.create(0, false));

        // then
        assertDoesNotThrow(() -> OrderLineItem.create(order, 1L, "두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), 1L));
    }
}
