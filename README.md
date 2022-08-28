### 📝 개발 일지

<details>
<summary>✍️ 1. 회원 엔티티 구현</summary>
<br>

ID, 이메일, 패스워드, 이름, 휴대전화번호 속성을 가진다.

회원가입 시 입력 값에 대한 유효성을 검증하기 위해 커스텀 어노테이션을 만들었다.

ConstraintValidator을 구현한 클래스에서 회원 리포지토리를 주입받기 위해 LocalValidatorFactoryBean을 컨테이너에 등록했다.
</details>

<details>
<summary>✍️ 2. 속성 컨버터 구현 </summary>
<br>

회원의 속성에 성별을 추가하고 Enum으로 코드를 관리한다.

엔티티를 저장할 때 값을 코드로 변환하기 위해서 컨버터를 만들고 전역으로 설정했다.

모든 Enum 마다 컨버터를 만들면 관리해야 하는 클래스의 개수가 많아져서 공통으로 사용할 수 있도록 변경했다. 
</details>
