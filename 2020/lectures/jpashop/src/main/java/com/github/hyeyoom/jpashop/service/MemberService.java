package com.github.hyeyoom.jpashop.service;

import com.github.hyeyoom.jpashop.domain.Member;
import com.github.hyeyoom.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    @Transactional
    public Long signUp(Member member) {
        validateDuplication(member);
        repository.save(member);
        return member.getId();
    }

    private void validateDuplication(Member member) {
        final List<Member> members = repository.findByName(member.getName());
        if (members.size() > 0) {
            throw new IllegalStateException("Name already taken.");
        }
    }

    public List<Member> findMembers() {
        return repository.findAll();
    }

    public Member findMember(Long id) {
        return repository.findOne(id);
    }

    @Transactional
    public void update(Long id, String name) {
        final Member member = repository.findOne(id);
        member.setName(name);
    }
}
