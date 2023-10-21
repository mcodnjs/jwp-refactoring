package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemsCreateRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있는 경우 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItemsCreateRequest> orderLineItemsRequest = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemsCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsRequest.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemsCreateRequest orderLineItem : orderLineItemsRequest) {
            final Long menuId = orderLineItem.getMenuId();
            final Long quantity = orderLineItem.getQuantity();
            // Todo: SaveAll
            OrderLineItem orderLineItem1 = OrderLineItem.create(menuId, quantity);
            orderLineItemDao.save(orderLineItem1);
            orderLineItems.add(orderLineItem1);
        }

        Order order = Order.create(orderTable.getId(), orderLineItems);

        final Order savedOrder = orderDao.save(order);
        final Long orderId = savedOrder.getId();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
        }
        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeOrderStatus(orderStatus.name());
        orderDao.save(order);
        return order;
    }
}
