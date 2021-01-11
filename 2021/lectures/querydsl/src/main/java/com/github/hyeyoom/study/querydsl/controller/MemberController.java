package com.github.hyeyoom.study.querydsl.controller;

import com.github.hyeyoom.study.querydsl.dto.MemberSearchCondition;
import com.github.hyeyoom.study.querydsl.dto.MemberTeamDto;
import com.github.hyeyoom.study.querydsl.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository repository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return repository.search(condition);
    }
}
