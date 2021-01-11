package com.github.hyeyoom.study.querydsl.controller;

import com.github.hyeyoom.study.querydsl.entity.Member;
import com.github.hyeyoom.study.querydsl.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService service;

    @PostConstruct
    public void init() {
        service.init();
    }

    @Service
    static class InitMemberService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            final Team teamA = new Team("teamA");
            final Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                final Team team = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i + 1, team));
            }
        }
    }
}
