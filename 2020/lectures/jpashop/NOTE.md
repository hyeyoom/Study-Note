# 1. 지연 로딩과 조회 성능 최적화

> 주의: 서로 참조하는 부분은 @JsonIgnore를 걸자.  
> 하지만 이는 `ui`기 때문에 엔티티에 거는 방법은 좋지 못함.  

> 참고: 엔티티 직접 노출 검지. DTO로 변환.

> 주의: 지연 로딩(LAZY) 피한다고 즉시 로딩(EAGER)로 변경 검지  
> 성능 최적화는 fetch join 사용할 것

그렇다면 DTO 변환은 확실한 해결책일까?
- 너무 많은 쿼리가 나간다.  

## 1.1. lazy가 어떻게 동작했는가?

1. Order 조회 -> SQL 1번 -> 결과 rows: 2
2. [loop] Order
    - member 쿼리
    - delivery 쿼리
    
이게 바로 N + 1문제. 초기 SQL 1회 + 멤버N + 배송N. 따라서 위 경우는 총 5회 (worst-case).  
(worst-case인 이유는 영속성 컨텍스트(pc)에 엔티티가 존재하면 DB로 쿼리가 안나가기 때문)  

## 1.2. 그래서 fetch join

## 1.3. new로 DTO 직접 변환

```java
return em.createQuery(
                "SELECT new com.github.hyeyoom.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderedDate, o.status, d.address)" +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delivery d",
                OrderSimpleQueryDto.class
        ).getResultList();
```

## 1.4. 결론

그래서 뭐가 좋을까?

- 네트워크 용량?
- 읽기가 빈번한가?

join이 성능을 잡아 먹는 포인트지 네트워크 용량의 차이는 사실 미미함  

쿼리 방식 선택  
1. entity -> DTO
2. 필요하면 `fetch join`으로 최적화
3. 그래도 안되면 DTO 직접 조회 방식으로 최적화
4. 그래도 안되면 JPA의 Native SQL나 JDBC template을 사용해 SQL 직접 사용