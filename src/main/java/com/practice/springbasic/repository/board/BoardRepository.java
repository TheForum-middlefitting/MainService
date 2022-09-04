package com.practice.springbasic.repository.board;

import com.practice.springbasic.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom, QuerydslPredicateExecutor<Board> {

}
