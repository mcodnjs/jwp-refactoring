package kitchenpos.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsSize(orderLineItems.size());
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(final OrderTable orderTable) {
        validateOrderTableIsEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    private void validateOrderTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있는 경우 주문을 등록할 수 없습니다.");
        }
    }

    public static Order create(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, COOKING, LocalDateTime.now(), orderLineItems);
    }

    public static Order create(final OrderTable orderTable) {
        return new Order(orderTable);
    }


    private void validateOrderLineItemsSize(final int orderLineItemsSize) {
        if (orderLineItemsSize < 1) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        // TODO: meal -> cooking의 상태를 가능하게 할 것인가?
        if (COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsSize(orderLineItems.size());
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
