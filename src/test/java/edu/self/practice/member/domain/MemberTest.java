package edu.self.practice.member.domain;

import edu.self.practice.member.dto.MemberRequest;
import edu.self.practice.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회원 도메인")
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberTest {
    private static final MemberRequest 회원가입_요청 = new MemberRequest("admin@mail.com", "admin1234", "admin", "010-1234-5678");
    private static final MemberRequest 비밀번호_미입력_회원가입_요청 = new MemberRequest("admin@mail.com", null, "admin", "010-1234-5678");

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Validator validator;

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void join() {
        // given
        Member member = new Member(회원가입_요청);

        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("이미 등록된 이메일은 회원가입 하는데 사용할 수 없다.")
    @Test
    void join_throwsException_givenExistedEmail() {
        // given
        memberRepository.save(new Member(회원가입_요청));

        // when & then
        assertThatThrownBy(() -> memberRepository.save(new Member(회원가입_요청))).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("check_nullability 속성을 true로 하면 엔티티를 영속화 하기 전에 널 값을 검사할 수 있다.")
    @Test
    void join_throwsException_givenNullPassword() {
        // given
        Member member = new Member(비밀번호_미입력_회원가입_요청);

        // when & then
        assertThatThrownBy(() -> memberRepository.save(member)).isInstanceOf(DataIntegrityViolationException.class)
                                                               .hasMessageContaining("not-null property references a null or transient value");
    }

    @DisplayName("커스텀 어노테이션을 사용해서 입력 값에 대한 유효성을 검증할 수 있다.")
    @ParameterizedTest
    @MethodSource("provideInvalidMemberRequest")
    void validate(String email, String password, String name, String phone) {
        // given
        MemberRequest request = new MemberRequest(email, password, name, phone);

        // when
        Set<ConstraintViolation<MemberRequest>> validate = validator.validate(request);

        // then
        assertThat(validate.isEmpty()).isFalse();
    }

    private static Stream<Arguments> provideInvalidMemberRequest() {
        return Stream.of(
                Arguments.of("admin@mail.com", "admin1234", "admin", null),
                Arguments.of("admin@mail.com", "admin1234", "admin", "가나다라"),
                Arguments.of("admin@mail.com", "admin1234", "admin", "01012345678")
        );
    }
}
