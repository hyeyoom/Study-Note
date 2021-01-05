package com.github.hyeyoom.study.datajpa.repository;

import com.github.hyeyoom.study.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
