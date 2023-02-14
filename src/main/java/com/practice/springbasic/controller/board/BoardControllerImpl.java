package com.practice.springbasic.controller.board;

import com.practice.springbasic.controller.board.vo.RequestBoardForm;
import com.practice.springbasic.controller.board.vo.ResponseBoardForm;
import com.practice.springbasic.controller.utils.form.SuccessReturnForm;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.board.BoardCategory;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.board.dto.BoardPageDto;
import com.practice.springbasic.repository.board.dto.BoardPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.board.dto.BoardDto;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.utils.check.CommonCheckUtil;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/board-service")
@Validated
public class BoardControllerImpl implements BoardController{
    private final BoardService boardService;
    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final Environment env;
    private final JwtUtils jwtUtils;

    public BoardControllerImpl(BoardService boardService, MemberService memberService, ModelMapper modelMapper, Environment env, JwtUtils jwtUtils) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.env = env;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @PostMapping("/boards")
    public ResponseEntity<ResponseBoardForm> postBoard
            (
            HttpServletRequest request,
            @RequestBody RequestBoardForm boardForm,
            BindingResult bindingResult
            ) {
        Long memberId = jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Member member = memberService.findMemberById(memberId).orElse(null);
        CommonCheckUtil.nullCheck400(member, "AuthFailed");
        BoardDto boardDto = modelMapper.map(boardForm, BoardDto.class);
        Board board = boardService.postBoard(member, boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBoardForm(board));
    }

    @Override
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<ResponseBoardForm> getBoard(@PathVariable Long boardId) {
        Board board = boardService.findBoard(boardId).orElse(null);
        CommonCheckUtil.nullCheck404(board, "BoardNotFound");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBoardForm(board));
    }

    @Override
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<ResponseBoardForm> updateBoard
            (HttpServletRequest request,
             @RequestBody RequestBoardForm boardUpdateForm,
             @PathVariable Long boardId,
             BindingResult bindingResult
            ) {
        jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        String email = jwtUtils.getTokenEmail(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Board board = boardService.findBoard(boardId).orElse(null);
        CommonCheckUtil.nullCheck404(board, "BoardNotFound");
        CommonCheckUtil.equalCheck401(email, board.getMember().getEmail(), "AuthFailed");
        BoardDto boardUpdateDto = modelMapper.map(boardUpdateForm, BoardDto.class);
        board = boardService.updateBoard(board, boardUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBoardForm(board));
    }

    @Override
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<SuccessReturnForm> deleteBoard(HttpServletRequest request, @PathVariable Long boardId) {
        jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        String email = jwtUtils.getTokenEmail(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Board board = boardService.findBoard(boardId).orElse(null);
        CommonCheckUtil.nullCheck404(board, "BoardNotFound");
        CommonCheckUtil.equalCheck401(email, board.getMember().getEmail(), "AuthFailed");
        boardService.deleteBoard(board);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessReturnForm(200));
    }

    @Override
    @GetMapping("/boards")
    public ResponseEntity<Page<BoardPageDto>> searchBoardPage
            (
            @PageableDefault(page = 0, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "boardWriterNickname", required = false) String boardWriterNickname,
            @RequestParam(name = "boardTitle", required = false) String boardTitle,
            @RequestParam(name = "boardContent", required = false) String boardContent,
            @RequestParam(name = "boardCategory", defaultValue = "free", required = false) BoardCategory boardCategory
            )
    {
        BoardPageSearchCondition condition = BoardPageSearchCondition
                .builder()
                .boardWriterNickname(boardWriterNickname)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardCategory(boardCategory)
                .build();
        Page<BoardPageDto> boardPage = boardService.findBoardPage(pageable, condition);
        return ResponseEntity.status(HttpStatus.OK).body(boardPage);
    }
}
