package com.github.hyeyoom.study.querydsl;

import com.github.hyeyoom.study.querydsl.entity.Member;
import com.github.hyeyoom.study.querydsl.entity.QMember;
import com.github.hyeyoom.study.querydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.github.hyeyoom.study.querydsl.entity.QMember.member;
import static com.github.hyeyoom.study.querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    @BeforeEach
    void setUp() {
        query = new JPAQueryFactory(em);

        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member m1 = new Member("m1", 10, teamA);
        final Member m2 = new Member("m2", 20, teamA);

        final Member m3 = new Member("m3", 30, teamB);
        final Member m4 = new Member("m4", 40, teamB);
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);
    }

    @Test
    void startJPQL() throws Exception {

        final Member foundMember =
                em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                        .setParameter("username", "m1")
                        .getSingleResult();

        assertThat(foundMember.getUsername()).isEqualTo("m1");
    }

    @Test
    void startQuerydsl() throws Exception {
        final Member foundMember = query
                .select(member)
                .from(member)
                .where(member.username.eq("m1"))
                .fetchOne();

        assertThat(foundMember.getUsername()).isEqualTo("m1");
    }

    @DisplayName("검색 조건 쿼리")
    @Test
    void search() throws Exception {

        final Member foundMember = query
                .selectFrom(member)
                .where(
                        member.username.eq("m1").and(member.age.eq(10))
                )
                .fetchOne();

        assertThat(foundMember.getUsername()).isEqualTo("m1");
    }

    @DisplayName("결과 조회")
    @Test
    void fetch() throws Exception {

        // fetch
        final List<Member> membersByFetch = query.selectFrom(member).fetch();
        for (Member member : membersByFetch) {
            System.out.println("member = " + member);
        }

        // fetch count
        final long count = query.selectFrom(member).fetchCount();
        System.out.println("count = " + count);

        // query result
        final QueryResults<Member> queryResults = query.selectFrom(member).fetchResults();
        System.out.println("queryResults = " + queryResults.getTotal());
        for (Member member : membersByFetch) {
            System.out.println("member = " + member);
        }
    }

    /**
     * age - desc
     * name - asc
     * null
     */
    @DisplayName("정렬")
    @Test
    void sorting() throws Exception {

        em.persist(new Member(null, 100));
        em.persist(new Member("m5", 20));
        em.persist(new Member("m6", 20));

        final List<Member> members = query.selectFrom(member)
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @DisplayName("페이징")
    @Test
    void paging() throws Exception {

        final JPAQuery<Member> q = query
                .selectFrom(member)
                .orderBy(member.username.desc().nullsLast());

        for (Member member : q.fetch()) {
            System.out.println("member = " + member);
        }

        final List<Member> members = q
                .offset(1)
                .limit(2)
                .fetch();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @DisplayName("집계함수 aggregation")
    @Test
    void aggregation() throws Exception {
        final List<Tuple> tuples = query
                .select(
                        member.age.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()

                )
                .from(member)
                .fetch();
        final Tuple tuple = tuples.get(0);

        System.out.println(tuple.get(member.age.count()));
        System.out.println(tuple.get(member.age.sum()));
        System.out.println(tuple.get(member.age.avg()));
        System.out.println(tuple.get(member.age.max()));
        System.out.println(tuple.get(member.age.min()));
    }

    @DisplayName("그룹 절")
    @Test
    void groupingBy() throws Exception {
        final List<Tuple> tuples = query
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        for (Tuple tuple : tuples) {
            System.out.println("tuple = " + tuple.get(team.name));
            System.out.println("tuple = " + tuple.get(member.age.avg()));
        }
    }

    @DisplayName("조인")
    @Test
    void join() throws Exception {
        final List<Member> members = query
                .selectFrom(member)
                .leftJoin(member.team, team)
                .fetch();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }
    }

    @DisplayName("theta 조인")
    @Test
    void thetaJoin() throws Exception {
        em.persist(new Member("teamA", 11111));
        em.persist(new Member("teamB", 22222));

        final List<Member> members = query
                .selectFrom(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    /**
     * 회원, 팀 조회. 팀 이름이 teamA인 팀만 조인, 회원 모두 조회
     *
     * @throws Exception
     */
    @DisplayName("JOIN ON")
    @Test
    void joinOn() throws Exception {
        final List<Tuple> tuples = query
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
        for (Tuple tuple : tuples) {
            System.out.println("tuple = " + tuple);
        }
    }

    @DisplayName("막조인")
    @Test
    void join_on_no_rel() throws Exception {
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @DisplayName("fetch 조인 미적용")
    @Test
    void fetchJoinNope() throws Exception {
        em.flush();
        em.clear();

        final Member foundMember = query
                .selectFrom(member)
                .where(member.username.eq("m1"))
                .fetchOne();

        final boolean loaded = emf.getPersistenceUnitUtil().isLoaded(foundMember.getTeam());
        System.out.println("--------------------------------");
        assertThat(loaded).as("페치 조인 미적용").isFalse();
        System.out.println("--------------------------------");
        System.out.println("foundMember = " + foundMember);
        System.out.println("--------------------------------");
        System.out.println("foundMember.team = " + foundMember.getTeam());
    }

    @DisplayName("fetch 조인 적용")
    @Test
    void fetchJoinDope() throws Exception {
        em.flush();
        em.clear();

        final Member foundMember = query
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("m1"))
                .fetchOne();

        System.out.println("--------------------------------");
        System.out.println("foundMember = " + foundMember);
        System.out.println("--------------------------------");
        System.out.println("foundMember.team = " + foundMember.getTeam());
    }

    @DisplayName("서브쿼리 - 나이가 가장 많은 회원")
    @Test
    void testSubquery() throws Exception {

        final QMember sub = new QMember("sub");

        final List<Member> members = query
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(sub.age.max())
                                .from(sub)
                ))
                .fetch();

        for (Member member1 : members) {
            System.out.println("member1 = " + member1);
        }
    }

    @DisplayName("서브쿼리 - 나이가 평균 이상")
    @Test
    void testSubquery2() throws Exception {

        final QMember sub = new QMember("sub");

        final List<Member> members = query
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(sub.age.avg())
                                .from(sub)
                ))
                .fetch();

        for (Member member1 : members) {
            System.out.println("member1 = " + member1);
        }
    }

    @DisplayName("case - when simple")
    @Test
    void testCaseWhen() throws Exception {
        final List<Tuple> fetch = query
                .select(member.age
                                .when(10).then("열 살")
                                .when(20).then("스무살")
                                .otherwise("기타"),
                        member
                )
                .from(member)
                .fetch();
        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }

    }

    @DisplayName("case - when complex")
    @Test
    void testCaseComplex() throws Exception {
        final List<String> fetch = query
                .select(new CaseBuilder()
                        .when(member.age.between(0, 19)).then("얼라")
                        .when(member.age.between(20, 29)).then("20대")
                        .otherwise("나머지")
                )
                .from(member)
                .fetch();
        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }

    @DisplayName("상수, 문자 처리 2")
    @Test
    void testString() throws Exception {

        final List<String> fetch = query
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();
        for (String s : fetch) {
            System.out.println("s = " + s);
        }
    }
}
