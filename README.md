### 프로젝트 개요: SecondChance (티켓 양도 서비스)

사용자가 자신이 구매했지만 갈 수 없게 된 공연 티켓이나 유효기간이 임박한 기프티콘을 다른 사람에게 양도하는 플랫폼입니다.

이 기획이 학습에 좋은 이유는 **'시간'과 '상태'에 따른 제약 사항이 명확**하기 때문입니다.

---

### 1. 🏗️ 도메인 모델 & 비즈니스 제약 사항 (핵심 학습 포인트)

이 부분은 `Service` 레이어가 아닌 **`Domain Entity`** 내부에 로직으로 구현해야 합니다.

#### A. 상품(Ticket) 도메인 규칙

1. **가격 정책 (Price Policy):**
    - 양도 가격은 정가(Original Price)를 초과할 수 없다. (프리미엄 방지)
    - 공연 당일에는 가격을 정가의 50% 이하로만 설정할 수 있다. (떨이 판매 규칙)

2. **상태 전이 (State Transition):**
    - 티켓은 `판매중(ON_SALE)` -> `예약중(RESERVED)` -> `판매완료(SOLD)` 또는 `만료(EXPIRED)`의 상태를 가진다.
    - `예약중` 상태에서는 다른 사람이 구매할 수 없다.
    - `판매완료` 상태에서는 가격 수정이나 삭제가 불가능하다.

3. **시간 제약 (Time Constraint):**
    - 공연 시작 1시간 전까지만 판매 등록이 가능하다.
    - 유효기간이 지난 티켓은 자동으로 `만료(EXPIRED)` 처리되거나 등록을 거부해야 한다.

#### B. 거래(Deal) 도메인 규칙
1. **구매 제한:** 판매자 본인은 자신의 티켓을 구매할 수 없다.
2. **예약 만료:** 구매자가 예약 후 10분 이내에 결제하지 않으면, 티켓은 다시 `판매중` 상태로 돌아가야 한다.

---

### 2. 🧱 아키텍처 및 기술적 요구사항 (레이어 구분 연습)

`Domain`, `Infrastructure`, `Presentation`, `Application` 4계층을 엄격히 구분해 봅니다.

#### A. Infrastructure Layer (외부 시스템 연동)

실제 외부 API가 없으므로 **Mock API 서버**를 흉내 내거나 인터페이스를 활용합니다.

- **공연 정보 검증기 (`EventProvider`):**
    - 사용자가 "아이유 콘서트" 티켓을 올리면, 실제 존재하는 콘서트인지, 날짜가 맞는지 확인하는 로직.
    - _구현:_ `ExternalEventApi` 인터페이스를 만들고, 구현체에서 무작위로 true/false를 반환하거나 더미 데이터를 조회하도록 함.

- **결제 시스템 (`PaymentGateway`):**
    - 결제 요청을 보내고 성공/실패 응답을 받는 로직.

#### B. Presentation Layer (API)

- **DTO 사용 필수:** 엔티티(`Ticket`)를 직접 반환하지 않고 `TicketResponse`, `TicketRegisterRequest` 등 DTO를 만들어 변환합니다.

- **Global Exception Handling:** 도메인 로직에서 터진 예외(예: `PriceLimitExceededException`)를 잡아 적절한 HTTP 상태 코드(400 Bad Request)로 변환하는 `@RestControllerAdvice`를 구현합니다.
