package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupTest {

    private final OrderTable orderTable1 = OrderTable.create(1, true);
    private final OrderTable orderTable2 = OrderTable.create(1, true);

    @DisplayName("테이블 그룹 생성 시, 주문 테이블의 크기가 2개 미만이면 예외가 발생한다.")
    @Test
    void tableGroup_FailWithInvalidOrderTableSize() {
        // given
        List<OrderTable> invalidOrderTable = List.of(orderTable1);

        // when & then
        assertThatThrownBy(() -> TableGroup.createWithGrouping(invalidOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다.");
    }

    @DisplayName("테이블 그룹을 생성할 수 있다.")
    @Test
    void tableGroup() {
        // then
        assertDoesNotThrow(() -> TableGroup.createWithGrouping(List.of(orderTable1, orderTable2)));
    }

    @DisplayName("주문 테이블을 테이블 그룹으로 그룹화할 수 있다.")
    @Test
    void group() {
        // given
        TableGroup tableGroupWithoutGrouping = TableGroup.createWithoutGrouping();

        // when & then
        assertDoesNotThrow(() -> tableGroupWithoutGrouping.group(List.of(orderTable1, orderTable2)));
        Assertions.assertThat(tableGroupWithoutGrouping.getOrderTables()).hasSize(2);
    }

    @DisplayName("그룹화 시, 주문 테이블의 크기가 2개 미만이면 예외가 발생한다.")
    @Test
    void group_FailWithOrderTableSize() {
        // given
        TableGroup tableGroupWithoutGrouping = TableGroup.createWithoutGrouping();

        // when & then
        assertThatThrownBy(() -> tableGroupWithoutGrouping.group(List.of(orderTable1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다.");
    }
}
