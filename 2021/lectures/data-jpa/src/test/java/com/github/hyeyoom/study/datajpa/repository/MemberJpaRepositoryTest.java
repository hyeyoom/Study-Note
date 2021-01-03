package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
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
    MemberRepository repository;

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
}