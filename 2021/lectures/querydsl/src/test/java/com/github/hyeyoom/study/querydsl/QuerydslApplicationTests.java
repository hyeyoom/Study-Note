package com.github.hyeyoom.study.querydsl;

import com.github.hyeyoom.study.querydsl.entity.Hello;
import com.github.hyeyoom.study.querydsl.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class QuerydslApplicationTests {

    @Autowired
    private EntityManager em;

    @Test
    void contextLoads() {
        final Hello hello = new Hello();
        em.persist(hello);

        final JPAQueryFactory query = new JPAQueryFactory(em);
        final QHello qHello = new QHello("h");

        final Hello result = query.selectFrom(qHello).fetchOne();

        assertThat(result).isEqualTo(hello);
        assertThat(result.getId()).isEqualTo(hello.getId());
        System.out.println(result.getId());
    }



}
