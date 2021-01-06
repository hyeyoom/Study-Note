package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.MemberProjection;
import com.github.hyeyoom.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

    Member findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    @Query(value = "SELECT * FROM member WHERE username = ? AND 1 = 1", nativeQuery = true)
    List<Member> findNativeQueryByUsername(String username);

    @Query(
            value = "SELECT m.member_id as id, m.username, t.name as teamName FROM member m LEFT JOIN team t",
            countQuery = "SELECT count(*) FROM member",
            nativeQuery = true
    )
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
