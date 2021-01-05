# 휘갈기는 메모

- Query Method
    - Spring Data Jpa에서 `findBy-`만 선언해도 구현체 들어가는 기능  
    - 이게 쿼리메소드다.

# 1. 쿼리 메소드

## 1.1. 페이징

page, slice

slice에 limit n + 1을 하는 이유는 다음 페이지 존재 여부를 알아내기 위해서. -> like infinity scrolling

## 1.2. 벌크 연산

> 주의사항: 변경 감지를 사용할 수 없게 됨

entity manager를 직접 다루는 경우에는 clear해주면 되겠지만 SDJ는 다르다.  

```java
@Modifying(clearAutomatically = true)
```

