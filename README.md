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

<details>
<summary>✍️ 3. OneToOne에서의 지연 로딩 이슈</summary>
<br>

일대일 양방향 연관 관계에서 주인이 아닌 엔티티를 조회할 때는 지연 로딩이 발생하지 않는다.

해당 엔티티의 테이블을 조회했을 때 연관된 엔티티가 실제로 존재하는지 알 수 없기 때문에 NULL이나 프록시 객체 중 어떤 것을 할당해야 할지 알 수 없고 그로 인해 N+1 문제가 발생한다.

일대다 양방향 연관 관계에서는 지연 로딩이 가능한데 그 이유는 컬렉션 래퍼를 사용하기 때문이다.

하이버네이트는 엔티티를 영속 상태로 만들 때 엔티티에 컬렉션이 있으면 컬렉션을 추적하고 관리할 목적으로 원본 컬렉션을 하이버네이트가 제공하는 내장 컬렉션으로 변경하는데, 이를 컬렉션 래퍼 라고 한다.

컬렉션 자체를 호출해도 컬렉션은 초기화 되지 않으며 .get(0)와 같이 컬렉션에서 실제 데이터를 조회할 때 데이터베이스를 조회해서 초기화 해준다.
</details>

<details>
<summary>✍️ 4. N+1 문제</summary>
<br>

연관 관계가 설정된 엔티티를 조회할 때 의도하지 않은 조회 쿼리가 추가로 발생하는 것을 N+1 문제라고 한다.

JPA는 JPQL을 분석해서 SQL을 생성하는데 이때 즉시 로딩, 지연 로딩과 같은 글로벌 패치 전략은 참고하지 않는다.

기본적으로 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하기 위해 플러시가 발생하고 데이터베이스에서 조회한 결과를 영속성 컨텍스트에 저장한다.

이때 영속성 컨텍스트에 엔티티가 존재하면 해당 엔티티가 수정 중인 경우가 있을 수 있기 때문에 조회한 결과를 사용하지 않는다.

문제는 영속성 컨텍스트에 저장하는 과정에서 즉시 로딩은 연관 관계에 있는 엔티티가 영속성 컨텍스트에 존재하지 않는다면 조회 쿼리가 발생한다.

지연 로딩을 사용하더라도 엔티티가 사용되는 시점에 조회 쿼리가 발생하기 때문에 똑같은 문제가 발생할 수 밖에 없다.

***Fetch Join***

```
@Query("select distinct p from Profile p join fetch p.pictures")
List<Profile> findAllProfiles();
```

일반 Join은 Select하는 엔티티만 조회하여 영속화하고 데이터는 필요하지 않지만 검색 조건에 필요한 경우 사용한다.

Fetch Join은 연관된 엔티티도 모두 영속화하는데 Set이나 distinct를 사용하여 중복된 데이터를 제거해야 한다.

ToMany에서 Fetch Join은 모든 데이터를 메모리상에 로드하고 페이징 처리하기 때문에 Out Of Memory를 유발할 수 있다.

둘 이상의 컬렉션은 MultipleBagFetchException이 발생하기 때문에 Fetch Join할 수 없다.

***EntityGraph***

```
@Query("select distinct p from Profile p")
@EntityGraph(attributePaths = "pictures", type = EntityGraph.EntityGraphType.FETCH)
List<Profile> findAllProfiles();
```

type은 EntityGraphType.LOAD, EntityGraphType.FETCH 2가지가 있다.

- LOAD : attributePaths에 정의한 엔티티들은 EAGER, 나머지는 글로벌 패치 전략을 따른다.
- FETCH : attributePaths에 정의한 엔티티들은 EAGER, 나머지는 LAZY로 패치한다.

Fetch Join과 다른 점은 inner join으로 동작하지만 EntityGraph는 left outer join으로 동작한다.

***BatchSize***

```
@BatchSize(size = 3)
@OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
private List<Picture> pictures = new LinkedList<>();
```

BatchSize 어노테이션을 사용하면 연관된 엔티티를 조회할 때 지정된 size만큼 SQL의 IN절을 사용해서 조회한다.

size는 IN절에 올수있는 최대 인자 개수를 의미하고 즉시 로딩은 size만큼 나누어 조회하고 지연 로딩은 최초에 size만큼 조회하고 그 다음 사용 시점에 다시 조회한다.

hibernate.default_batch_fetch_size 속성을 사용하면 애플리케이션 전체에 size를 적용할 수 있다.

</details>

<details>
<summary>✍️ 5. 패스워드 암호화, 복호화 구현</summary>
<br>

***암호문의 성질***

패스워드 암호화, 복호화를 구현하기 전에 암호문의 성질에 대해 간략히 알아본다.

안전한 암호문은 공격자가 이를 보고 원본 메시지나 암호화에 사용된 키를 유추할 수 없어야 하는데 이러한 성질을 혼돈과 확산이라고 한다.

혼돈은 암호문으로부터 키를 알아낼 수 없게 하는 성질이고 확산은 암호문으로부터 원문을 알아낼 수 없게 하는 성질이다.

- 안전한 암호는 공격자가 아무리 많은 평문-암호문 쌍을 알고 있어도 그 속에서 키의 패턴을 발견할 수 없어야 한다.
- 서로 다른 원문이 비슷한 내용을 담고 있더라도 각각의 암호문은 완전히 다른 값을 가져야 한다.

암호 알고리즘은 혼돈과 확산을 달성하기 위해 Substitution과 Permutation을 이용한다.

Substitution은 문자를 다른 문자로 바꾸는 것이고, Permutation은 문자들의 순서를 바꾸는 것인데 각각 한 번 수행하는 것이 암호 알고리즘의 기본 수행 단위이다.

암호문은 이를 여러번 수행할 수록 안전하고 Substitution-Permutation을 연속하여 수행하도록 이어 놓은 것을 SPN(Substitution Permutation Network)이라고 한다.

SPN을 이용하는 대표적인 암호 알고리즘으로 AES가 있는데 보통 데이터를 블록 단위로 처리하며 AES의 경우 블록의 크기는 128비트(16바이트)이다.

그런데 모든 데이터가 16바이트 크기를 가질 수 없으므로, 데이터를 블록 단위로 나누어 처리하고 합치는 과정이 필요하다.

***Cipher***

대칭키 알고리즘은 동일한 키를 사용하여 데이터를 암호화하고 다시 원래 데이터로 복호화할 수 있다.

자바에서는 javax.crypto 패키지에 속한 Cipher 클래스를 사용하는데 thread-safe하지 않기 때문에 암호화, 복호화할 때마다 static 메서드를 호출하여 객체를 인스턴스 화한다.

이 때, 대칭키 알고리즘/운용 모드/패딩 방식을 전달하는데 알고리즘은 필수 입력값이며 나머지는 설정하지 않을 경우 기본값이 지정된다.

***운용 모드***

데이터를 블록으로 나누어 처리하고 합치는 것이 운용 모드가 하는 역할인데 대표적인 운용 모드로는 ECB(Electronic Code Book)와 CBC(Cipher Block Chaining)가 있다.

- ECB(Electronic Code Book)
    - 단순히 블록 단위로 처리한 결과를 이어붙이는 방법
    - 동일한 암호화 키를 사용하기 때문에 병렬 처리가 가능
    - 동일한 데이터에 대해 항상 같은 암호문이 출력되기 때문에 보안에 취약

- CBC(Cipher Block Chaining)
    - 이전 블록의 암호화 결과를 활용하여 암호화하는 체이닝 모드
    - 암호화된 블록과 XOR 연산을 한 다음에 암호화하기 때문에 같은 데이터라도 전혀 다른 암호문이 된다
    - 최초의 블록은 암호화할 때에 XOR 연산 대상이 없기 때문에 iv(Initialization Vector) 값을 이용함

***패딩 방식***

데이터의 크기가 16바이트의 배수가 아니라면 마지막 블록은 16바이트보다 작은 크기가 된다.

이 때 마지막 블록의 빈 부분을 채워주는 방식을 패딩이라고 하는데 PKCS5과 PKCS7을 많이 사용한다.

PKCS5는 8바이트 블록의 암호 알고리즘을 가정하는데 원문의 길이가 L바이트이면 마지막 블록은 L mod 8의 크기를 갖는다.

데이터가 17바이트라면 2개의 블록과 1바이트가 암호화 대상이 되고 7바이트가 부족하므로 07을 7번 패딩한다.

데이타 길이가 블록 배수라면 한 개의 블록만큼 패딩 바이트를 추가해서 암호화하고 복호화시에는 제거한다.

PKCS7은 8바이트가 아닌 가변 길이를 갖는 다는 점에서 다르지만 동작 방식은 동일하다.

</details>
