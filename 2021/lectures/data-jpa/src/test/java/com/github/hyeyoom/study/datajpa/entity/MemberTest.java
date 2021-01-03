package com.github.hyeyoom.study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    void testEntity() throws Exception {
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member m1 = new Member("member1", 10, teamA);
        final Member m2 = new Member("member2", 20, teamA);
        final Member m3 = new Member("member3", 30, teamB);
        final Member m4 = new Member("member4", 40, teamB);

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);

        em.flush();
        em.clear();

        final List<Member> members =
                em.createQuery("SELECT m FROM Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member: " + member);
            System.out.println("-> member.team: " + member.getTeam());
        }

    }
}