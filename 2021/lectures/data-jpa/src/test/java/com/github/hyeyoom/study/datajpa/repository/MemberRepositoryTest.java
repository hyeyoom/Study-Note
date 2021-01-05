package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import com.github.hyeyoom.study.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository repository;
    @Autowired
    TeamRepository teamRepository;

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
    void findByAge() throws Exception {

        // given
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m2", 10, null));
        repository.save(new Member("m3", 10, null));
        repository.save(new Member("m4", 10, null));
        repository.save(new Member("m5", 10, null));

        // when
        final PageRequest pr = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        final Page<Member> page = repository.findByAge(10, pr);
        System.out.println("count = " + page.getTotalElements());
        System.out.println("pages = " + page.getTotalPages());
        final List<Member> members = page.getContent();

        for (Member member : members) {
            System.out.println("member = " + member);
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
        final int result = repository.bulkAgePlus(10);
        System.out.println("result = " + result);

        final List<Member> all = repository.findAll();
        for (Member member : all) {
            System.out.println("member = " + member);
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

        em.flush();
        em.clear();

        // when
        final List<Member> members = repository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam());
        }

        // then
    }
}