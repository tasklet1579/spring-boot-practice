package edu.self.practice.member.domain;

import edu.self.practice.member.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.PersistenceUnitUtil;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("프로필 도메인")
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("일대일 양방향 연관 관계에서 외래키를 가지고 있는 엔티티를 조회할 때는 지연 로딩이 발생한다.")
    @Test
    void lazyLoading() {
        // given
        PersistenceUnitUtil persistenceUnitUtil = testEntityManager.getEntityManager()
                                                                   .getEntityManagerFactory()
                                                                   .getPersistenceUnitUtil();

        // when
        List<Profile> profiles = profileRepository.findAll();

        // then
        profiles.forEach(profile -> assertFalse(persistenceUnitUtil.isLoaded(profile, "member")));
    }
}
