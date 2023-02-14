package com.practice.springbasic.controller.board;

import com.practice.springbasic.controller.board.vo.RequestBoardForm;
import com.practice.springbasic.controller.board.vo.ResponseBoardForm;
import com.practice.springbasic.controller.utils.form.SuccessReturnForm;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface BoardController {
    ResponseEntity<ResponseBoardForm> postBoard(HttpServletRequest request, @Valid RequestBoardForm boardForm, BindingResult bindingResult);

    ResponseEntity<ResponseBoardForm> getBoard(@NumberFormat Long boardId);

    ResponseEntity<ResponseBoardForm> updateBoard(HttpServletRequest request, @Valid RequestBoardForm boardUpdateForm, Long boardId, BindingResult bindingResult);

    ResponseEntity<SuccessReturnForm> deleteBoard(HttpServletRequest request, Long boardId);

//    ResponseEntity<Page<BoardPageDto>> searchBoardPage(Pageable pageable, @Valid BoardPageSearchCondition condition, BindingResult bindingResult);
    ResponseEntity<Page<BoardPageDto>> searchBoardPage(Pageable pageable,
                                                       String boardWriterNickname,
                                                       String boardTitle,
                                                       String boardContent,
                                                       BoardCategory boardCategory);
}
