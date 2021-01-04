package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

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
}