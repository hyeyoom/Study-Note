package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;

    @Test
    void testMember() throws Exception {

        final Member member = new Member("Hello");
        repository.save(member);

        final Long id = member.getId();
        final Member foundMember = repository.find(id);

        assertThat(member).isNotNull();
        assertThat(foundMember).isNotNull();
        assertThat(member.getId()).isEqualTo(foundMember.getId());
        assertThat(member.getName()).isEqualTo(foundMember.getName());
        assertThat(member).isEqualTo(foundMember);

    }
}