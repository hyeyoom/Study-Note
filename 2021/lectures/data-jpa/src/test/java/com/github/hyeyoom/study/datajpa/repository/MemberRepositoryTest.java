package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import com.github.hyeyoom.study.datajpa.entity.MemberProjection;
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

        flushAndClear();

        // when
        final List<Member> members = repository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team.class = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam());
        }

        // then
    }

    @Test
    @Transactional(readOnly = true)
    void queryHint() throws Exception {
        final Member foundUser = repository.findByUsername("member1");
        foundUser.setUsername("abcd");
    }

    @Test
    void lockTest() {
        final String memberName = "member1";
        final Member m1 = new Member(memberName, 10, null);
        repository.save(m1);
        flushAndClear();

        final List<Member> foundUser = repository.findLockByUsername(memberName);
        final Member member = foundUser.get(0);
        member.setUsername("asfd");
    }

    @Test
    void testCustom() {
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m2", 10, null));
        repository.save(new Member("m3", 10, null));

        flushAndClear();

        final List<Member> members = repository.findMemberCustom();
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void baseEntityTest() throws Exception {

        // given
        final Member m1 = new Member("m1");
        repository.save(m1);
        flushAndClear();

        final Member member = repository.findById(m1.getId()).get();
        member.setUsername("updated");
        flushAndClear();

        final Member foundMember = repository.findById(m1.getId()).get();
        System.out.println("foundMember = " + foundMember.getCreatedDate());
        System.out.println("foundMember = " + foundMember.getLastModifiedDate());
        System.out.println("foundMember = " + foundMember.getCreatedBy());
        System.out.println("foundMember = " + foundMember.getLastModifiedBy());
    }

    @Test
    void testNative() {
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m1", 10, null));
        repository.save(new Member("m3", 10, null));

        final List<Member> members = repository.findNativeQueryByUsername("m1");
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void testNativeProjection() {
        final Team team = new Team("홍철없는홍철팀");
        teamRepository.save(team);

        repository.save(new Member("m1", 1, team));
        repository.save(new Member("m2", 2, team));
        repository.save(new Member("m3", 3, team));
        repository.save(new Member("m4", 4, team));
        repository.save(new Member("m5", 5, team));
        repository.save(new Member("m6", 6, team));
        repository.save(new Member("m7", 7, team));
        repository.save(new Member("m8", 8, team));

        final Page<MemberProjection> page = repository.findByNativeProjection(PageRequest.of(0, 5));
        for (MemberProjection projection : page) {
            System.out.println("projection = " + projection.getUsername());
            System.out.println("projection = " + projection.getTeamName());
        }
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

}