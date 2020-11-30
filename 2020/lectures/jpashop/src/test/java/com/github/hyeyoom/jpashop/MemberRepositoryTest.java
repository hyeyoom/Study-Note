package com.github.hyeyoom.jpashop;

import com.github.hyeyoom.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Test
    @Transactional  // em를 통한 모든 데이터 변경은 transaction 내에서 이루어지기 때문에
    void testMember() {
        // given
        final Member member = new Member();

        // when
        final Long id = repository.save(member);
        final Member foundMember = repository.find(id);

        // then
        Assertions.assertThat(foundMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(foundMember).isEqualTo(member);
    }
}