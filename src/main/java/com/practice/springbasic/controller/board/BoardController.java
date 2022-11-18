package com.practice.springbasic.controller.board;

import com.practice.springbasic.controller.utils.form.SuccessResult;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.dto.BoardDto;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface BoardController {
    SuccessResult postBoard(HttpServletRequest request, BoardDto boardDto, BindingResult bindingResult);

    SuccessResult getBoard(Long boardId);

    SuccessResult updateBoard(HttpServletRequest request, BoardUpdateDto boardUpdateDto, Long boardId, BindingResult bindingResult);

    SuccessResult deleteBoard(HttpServletRequest request, Long boardId);
    SuccessResult searchBoardPage(Pageable pageable, BoardPageSearchCondition condition, BindingResult bindingResult);
}
