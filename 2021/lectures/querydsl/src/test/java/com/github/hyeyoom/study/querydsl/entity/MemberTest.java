package com.github.hyeyoom.study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private EntityManager em;

    @Test
    void testEntity() throws Exception {
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member m1 = new Member("m1", 10, teamA);
        final Member m2 = new Member("m2", 20, teamA);

        final Member m3 = new Member("m3", 30, teamB);
        final Member m4 = new Member("m4", 40, teamB);
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);

        flushAndClear();

        final List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}