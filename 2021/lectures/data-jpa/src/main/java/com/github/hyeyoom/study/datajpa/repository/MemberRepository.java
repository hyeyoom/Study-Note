package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(
            countQuery = "SELECT count(m) FROM Member m"
    )
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.age = m.age + 1 WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // @EntityGraph(attributePaths = "team")
    @Query("SELECT m FROM Member m JOIN FETCH m.team t")
    @Override
    List<Member> findAll();
}
