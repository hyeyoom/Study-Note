package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m LEFT JOIN FETCH m.team t", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id) {
        final Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("SELECT count(m) FROM Member m", Long.class).getSingleResult();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery(
                "SELECT m FROM Member m WHERE m.username = :username AND m.age > :age",
                Member.class
        ).setParameter("username", username).setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery(
                "SELECT m FROM Member m WHERE m.age = :age ORDER BY m.username DESC",
                Member.class
        )
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("SELECT count(m) FROM Member m WHERE m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgeIncrement(int age) {
        return em.createQuery("UPDATE Member m SET m.age = m.age + 1 WHERE m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
