package com.github.hyeyoom.study.querydsl.repository;

import com.github.hyeyoom.study.querydsl.dto.MemberSearchCondition;
import com.github.hyeyoom.study.querydsl.dto.MemberTeamDto;
import com.github.hyeyoom.study.querydsl.entity.Member;
import com.github.hyeyoom.study.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {


    @Autowired
    private EntityManager em;

    @Autowired
    MemberJpaRepository repository;

    @DisplayName("테스트")
    @Test
    void basicTest() throws Exception {
        final Member m1 = new Member("member1", 10);
        repository.save(m1);

        final Member foundMember = repository.findById(m1.getId()).get();
        assertThat(m1).isEqualTo(foundMember);

        final List<Member> result = repository.findAll();
        assertThat(result).containsExactly(m1);

        final List<Member> foundMember2 = repository.findByUsername("member1");
        assertThat(foundMember2).containsExactly(m1);
    }

    @DisplayName("Querydsl 테스트")
    @Test
    void basicQuerydslTest() throws Exception {
        final Member m1 = new Member("member1", 10);
        repository.save(m1);

        final Member foundMember = repository.findById(m1.getId()).get();
        assertThat(m1).isEqualTo(foundMember);

        final List<Member> result = repository.findAllByQuerydsl();
        assertThat(result).containsExactly(m1);

        final List<Member> foundMember2 = repository.findByUsernameByQuerydsl("member1");
        assertThat(foundMember2).containsExactly(m1);
    }

    @DisplayName("")
    @Test
    void searchTest() throws Exception {
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

        final MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        em.flush();
        em.clear();

        final List<MemberTeamDto> result = repository.search(condition);
        System.out.println(result.size());
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }
    }
}