package com.practice.springbasic.controller.board;

import com.practice.springbasic.controller.form.SuccessResult;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.service.board.dto.BoardDto;
import lombok.extern.java.Log;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public interface BoardController {
    SuccessResult postBoard(HttpServletRequest request, BoardDto boardDto, BindingResult bindingResult);

    SuccessResult getBoard(Long boardId);

    SuccessResult updateBoard(HttpServletRequest request, BoardUpdateDto boardUpdateDto, Long boardId, BindingResult bindingResult);

    SuccessResult deleteBoard(HttpServletRequest request, Long boardId);
}
