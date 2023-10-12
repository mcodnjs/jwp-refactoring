package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({MenuGroupService.class, JdbcTemplateMenuGroupDao.class})
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroup expected = new MenuGroup();
        expected.setName("메뉴 그룹");

        // when
        MenuGroup actual = menuGroupService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(menuGroupDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
        });
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("두마리메뉴");

        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("한마리메뉴");

        MenuGroup 한마리메뉴 = menuGroupService.create(menuGroup1);
        MenuGroup 두마리메뉴 = menuGroupService.create(menuGroup2);

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(한마리메뉴, 두마리메뉴));
        });
    }
}
