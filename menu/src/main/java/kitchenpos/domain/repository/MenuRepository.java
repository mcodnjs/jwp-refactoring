package kitchenpos.domain.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(List<Long> ids);

    List<Menu> findAllByIdIn(List<Long> ids);
}