### 📝 개발 일지

<details>
<summary>✍️ 1. 회원 엔티티 구현</summary>
<br>

ID, 이메일, 패스워드, 이름, 휴대전화번호 속성을 가진다.

회원가입 시 입력 값에 대한 유효성을 검증하기 위해 커스텀 어노테이션을 만들었다.

ConstraintValidator을 구현한 클래스에서 회원 리포지토리를 주입받기 위해 LocalValidatorFactoryBean을 컨테이너에 등록했다.
</details>
