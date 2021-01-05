package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import com.github.hyeyoom.study.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberJpaRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void testMember() throws Exception {
        final Member member = new Member("Hello");
        repository.save(member);

        final Long id = member.getId();
        final Member foundMember = repository.findById(id).get();

        assertThat(member).isNotNull();
        assertThat(foundMember).isNotNull();
        assertThat(member.getId()).isEqualTo(foundMember.getId());
        assertThat(member).isEqualTo(foundMember);
    }

    @Test
    void testFindByUsernameAndAgeGreaterThan() throws Exception {

        // given
        final Member m1 = new Member("m1", 10, null);
        final Member m2 = new Member("m2", 20, null);
        final Member m3 = new Member("m2", 30, null);
        repository.save(m1);
        repository.save(m2);
        repository.save(m3);

        // when
        em.flush();
        em.clear();

        final List<Member> members = repository.findByUsernameAndAgeGreaterThan("m2", 15);
        for (Member member : members) {
            System.out.println(member);
        }
        // then
    }

    @Test
    void findByPage() throws Exception {

        // given
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m2", 10, null));
        repository.save(new Member("m3", 10, null));
        repository.save(new Member("m4", 10, null));
        repository.save(new Member("m5", 10, null));

        // when
        final int age = 10;
        final int offset = 2;
        final int limit = 3;

        final List<Member> members = repository.findByPage(age, offset, limit);
        final long count = repository.totalCount(age);

        for (Member member : members) {
            System.out.println(member);
        }

        // then
    }

    @Test
    void bulkAgeIncrement() throws Exception {

        // given
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m2", 10, null));
        repository.save(new Member("m3", 10, null));
        repository.save(new Member("m4", 10, null));
        repository.save(new Member("m5", 10, null));

        // when
        final int result = repository.bulkAgeIncrement(10);
        System.out.println("result = " + result);

        final List<Member> all = repository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member.getAge());
        }
    }

    @Test
    void testLazyLoading() throws Exception {

        // given
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        final Member m1 = new Member("m1", 10, teamA);
        final Member m2 = new Member("m2", 10, teamB);
        repository.save(m1);
        repository.save(m2);

        // when
        em.flush();
        em.clear();

        final List<Member> members = repository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }

        // then
    }
}