package com.practice.springbasic.controller.board;

import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.utils.jwt.JwtUtils;
import com.practice.springbasic.controller.board.dto.ReturnSingleBoardForm;
import com.practice.springbasic.controller.utils.check.CheckUtil;
import com.practice.springbasic.controller.utils.form.SuccessResult;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.dto.BoardUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.board.dto.BoardDto;
import com.practice.springbasic.service.member.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/board-service")
public class BoardControllerImpl implements BoardController{
    private final BoardService boardService;
    private final MemberService memberService;

    public BoardControllerImpl(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @Override
    @PostMapping("/boards")
    public SuccessResult postBoard(HttpServletRequest request, @RequestBody @Validated BoardDto boardDto, BindingResult bindingResult) {
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        Long memberId = JwtUtils.verifyJwtToken(request, JwtProperties.ACCESS_HEADER_STRING);
        Member member = memberService.findMemberById(memberId).orElse(null);
        CheckUtil.nullCheck(member);
        Board board = boardService.postBoard(member, boardDto);
        return new SuccessResult(new ReturnSingleBoardForm(board));
    }

    @Override
    @GetMapping("/boards/{boardId}")
    public SuccessResult getBoard(@PathVariable Long boardId) {
        Board board = boardService.findBoard(boardId).orElse(null);
        CheckUtil.nullCheck(board);
        return new SuccessResult(new ReturnSingleBoardForm(board));
    }

    @Override
    @PutMapping("/boards/{boardId}")
    public SuccessResult updateBoard(HttpServletRequest request, @RequestBody @Validated BoardUpdateDto boardUpdateDto, @PathVariable Long boardId, BindingResult bindingResult) {
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        JwtUtils.verifyJwtToken(request, JwtProperties.ACCESS_HEADER_STRING);
        String email = JwtUtils.getTokenEmail(request, JwtProperties.ACCESS_HEADER_STRING);
        Board preBoard = boardService.findBoard(boardId).orElse(null);
        CheckUtil.nullCheck(preBoard);
        if(!email.equals(preBoard.getMember().getEmail())) {
            throw new RuntimeException("정상적이지 않은 접근");
        }
        Board board = boardService.updateBoard(preBoard, boardUpdateDto);
        return new SuccessResult(new ReturnSingleBoardForm(board));
    }

    @Override
    @DeleteMapping("boards/{boardId}")
    public SuccessResult deleteBoard(HttpServletRequest request, @PathVariable Long boardId) {
        JwtUtils.verifyJwtToken(request, JwtProperties.ACCESS_HEADER_STRING);
        String email = JwtUtils.getTokenEmail(request, JwtProperties.ACCESS_HEADER_STRING);
        Board board = boardService.findBoard(boardId).orElse(null);
        CheckUtil.nullCheck(board);
        if(!email.equals(board.getMember().getEmail())) {
            throw new RuntimeException("정상적이지 않은 접근");
        }
        boardService.deleteBoard(board);
        return new SuccessResult(null);
    }

    @Override
    @PostMapping("boards/offset/")
    public SuccessResult searchBoardPage(@PageableDefault(page = 0, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable, @RequestBody @Validated BoardPageSearchCondition condition, BindingResult bindingResult) {
//        System.out.println(pageable.getSort());
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        Page<BoardPageDto> boardPage = boardService.findBoardPage(pageable, condition);
        return new SuccessResult(boardPage);
    }
}
