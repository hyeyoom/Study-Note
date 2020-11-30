package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    void 회원가입() throws Exception {

        // given
        final Member member = new Member();
        member.setName("Chiho Won");

        // when
        final Long memberId = memberService.signUp(member);

        // then
        assertEquals(member, memberRepository.findOne(memberId));
    }

    @Test
    void 중복_회원_예제() throws Exception {

        // given
        final Member member1 = new Member();
        member1.setName("Chiho Won");
        final Member member2 = new Member();
        member2.setName("Chiho Won");

        // when
        memberService.signUp(member1);

        // then
        Assertions.assertThatThrownBy(() -> memberService.signUp(member2)).isInstanceOf(IllegalStateException.class);
    }
}