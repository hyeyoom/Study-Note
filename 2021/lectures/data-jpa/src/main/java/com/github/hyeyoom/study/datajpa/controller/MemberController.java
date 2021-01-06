package com.github.hyeyoom.study.datajpa.controller;

import com.github.hyeyoom.study.datajpa.entity.Member;
import com.github.hyeyoom.study.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository repository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id) {
        final Member member = repository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostConstruct
    public void init() {
        /*for (int i = 0; i < 100; i++) {
            final int idx = i + 1;
            repository.save(new Member("member" + idx, idx, null));
        }*/
    }
}
