package com.github.hyeyoom.study.querydsl.repository;

import com.github.hyeyoom.study.querydsl.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}