package org.example.ticket.infra.repository

import org.example.ticket.domain.model.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaRepository: JpaRepository<Ticket, Long> {
    fun findByBarcode(barcode: String): Ticket? // ? 넣어야 null 처리되서 조회 안될때 에러가 안남
}

/* JPA 동작 방식
     스프링이 JPA 의존성을 보고 어떻게 이 라이브러리코드가 동작하도록 끌어오는가?
     1. 의존성에 JPA가 존재하는걸 확인
     2. JpaRepository 상속한 인터페이스를 찾음
     3. 런타임 메모리에 CRUD 메서드가 선언된 클래스 자동 생성
     4. 생성된 클래스 자바빈 등록
     5. JpaRepository를 상속받은 클래스(작성한 클래스)를 통해 CRUD가능
*/

/* 이해 안가던 점
    1. Repository를 CrudRepository가 확장
    2. CrudRepository를 ListCrudRepository가 확장
    3. JpaRepository가 ListCrudRepository를 확장

    A : JpaRepository -> TicketJpaRepository
    B : JpaRepository -> JpaRepositoryImplementation -> SimpleJpaRepository

    SimpleRepository(라이브러리)/TicketJpaRepository(내구현)

    1. TicketJpaRepository.save를 타고올라가면 CrudRepository.save가 나옴
    2. CrudRepository.save를 구현한건 SimpleJpaRepository임
    3. ? 그런데 우리가 쓰는건 TicketJpaRepository인데 SimpleJpaRepository.save가 어떻게 연결되지?
       (TicketJpaRepository.save는 선언안했는데?)

    => 스프링이 프록시를 자동 생성해주기 때문
        최상위 부터 동작을 추적하면

        1. Spring Boot Auto Configuration
        => "JPA 의존성 있네? @EnableJpaRepositories 자동 활성화"

        2. JpaRepositoriesRegistrar
        => "JpaRepository 상속한 인터페이스 스캔해" → TicketJpaRepository 발견

        3. JpaRepositoryFactoryBean (인터페이스당 하나 생성)
        => "TicketJpaRepository를 처리할 팩토리 만들자"

        4. JpaRepositoryFactory
        => SimpleJpaRepository 생성
        => 프록시로 감싸기
        => Bean 등록

      => 결론 : TicketJpaRepository.save가 SimpleJpaRepository.save로 호출되는건 단순하게 스프링 내부 동작임
        SimpleJpaRepository에 보면 라고 선언되어 있는데
        * Default implementation of the {@link org.springframework.data.repository.CrudRepository} interface. This will offer
        * you a more sophisticated interface than the plain {@link EntityManager} .
        이렇게 SimpleJpaRepository에가 CrudRepository의 기본 구현체라고 기입되어 있는데,
        => 프록시가 자동으로 연결해준단 말임
        => extends(확장/상속)가 아니라 delegation(위임)하는 형태
        => 이해하기 어려운 부분은 작성하는 코드에 보이는게 아니라 런타임 구현체를 스프링이 생성한다고 보는것
        => 본인 코드만 보면 이 동작을 절대 알 수 없음 => Spring Doc를 읽읍시다

*/


/*
JPA 내부동작
JPA의 핵심은 "객체와 DB 테이블을 매핑해서, SQL을 직접 안 짜고 객체로 DB를 다루게 해주는 것"
TicketJpaRepository → JpaRepository → ListCrudRepository → CrudRepository
전부 인터페이스 → 껍데기만 있음 → save()의 실제 코드가 없음
Spring이 해주는 건 이 껍데기에 SimpleJpaRepository라는 구현체를 연결해주는 것입니다.
  1. 매핑 (앱 시작 시)
  Ticket 클래스를 분석 (리플렉션)
    → @Entity 확인 → "이건 DB 테이블이구나"
    → 클래스명 Ticket → 테이블명 ticket
    → 필드 barcode: String → 컬럼 barcode VARCHAR
    → 필드 originalPrice: BigDecimal → 컬럼 original_price DECIMAL
    → 필드 id: Long + @Id → Primary Key

  결과: 객체 ↔ 테이블 매핑 정보를 메모리에 보관

  2. 영속성 컨텍스트 (핵심)

  JPA는 객체와 DB 사이에 중간 저장소를 둡니다:

  우리 코드 ↔ 영속성 컨텍스트(메모리) ↔ DB

  모든 동작이 이 영속성 컨텍스트를 거칩니다:

  save(ticket)    → 영속성 컨텍스트에 등록 → 커밋 시 INSERT
  findById(1L)    → 영속성 컨텍스트에 있으면 그거 반환 (DB 안 감)
                  → 없으면 DB 조회 → 영속성 컨텍스트에 저장 → 반환
  delete(ticket)  → 영속성 컨텍스트에서 삭제 표시 → 커밋 시 DELETE

  3. 변경 감지 (Dirty Checking)

  이게 JPA의 가장 강력한 기능입니다:

  val ticket = ticketRepository.findById(1L)  // DB에서 조회
  ticket.ticketStatus = TicketStatus.SOLD     // 필드만 바꿈
  // save() 안 해도 됨!
  // 트랜잭션 커밋 시 JPA가 "어? 값이 바뀌었네" → UPDATE 자동 실행

  조회 시: 원본 스냅샷을 저장해둠      {status: ON_SALE}
  커밋 시: 현재 상태와 스냅샷 비교     {status: SOLD} vs {status: ON_SALE}
           → "status 바뀌었네" → UPDATE ticket SET status = 'SOLD' WHERE id = 1

  4. 전체 흐름 정리

  트랜잭션 시작
    ↓
  조회/저장/수정/삭제 → 전부 영속성 컨텍스트에서 처리
    ↓
  트랜잭션 커밋
    ↓
  영속성 컨텍스트와 DB의 차이를 비교
    ↓
  필요한 SQL만 생성해서 DB에 실행 (INSERT, UPDATE, DELETE)
    ↓
  영속성 컨텍스트 초기화

  결국 JPA는 **"DB를 직접 건드리지 말고, 객체만 다뤄. 나머지는 내가 알아서 SQL로 바꿔줄게"**가 전부입니다.
  영속성 컨텍스트가 그 중간다리 역할을 하는 거고요.

 */