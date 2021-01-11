package com.github.hyeyoom.study.querydsl.repository;

import com.github.hyeyoom.study.querydsl.dto.MemberSearchCondition;
import com.github.hyeyoom.study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
