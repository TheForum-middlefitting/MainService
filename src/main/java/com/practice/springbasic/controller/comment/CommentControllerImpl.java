package com.practice.springbasic.controller.comment;

import com.practice.springbasic.controller.comment.dto.ReturnSingleCommentForm;
import com.practice.springbasic.controller.utils.check.CheckUtil;
import com.practice.springbasic.controller.utils.form.SuccessReturnForm;
import com.practice.springbasic.domain.board.Board;
import com.practice.springbasic.domain.comment.Comment;
import com.practice.springbasic.domain.comment.dto.CommentUpdateDto;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.repository.comment.dto.CommentPageDto;
import com.practice.springbasic.repository.comment.dto.CommentPageSearchCondition;
import com.practice.springbasic.service.board.BoardService;
import com.practice.springbasic.service.comment.CommentService;
import com.practice.springbasic.service.comment.dto.CommentDto;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.utils.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/comment-service")
@Validated
@Slf4j
public class CommentControllerImpl implements CommentController{
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final Environment env;

    public CommentControllerImpl(BoardService boardService, MemberService memberService, CommentService commentService, ModelMapper modelMapper, JwtUtils jwtUtils, Environment env) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
        this.env = env;
    }

    @Override
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<ReturnSingleCommentForm> postComment(HttpServletRequest request, @PathVariable Long boardId, @RequestBody CommentDto commentDto, BindingResult bindingResult) {
        Long memberId = jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Member member = memberService.findMemberById(memberId).orElse(null);
        Board board = boardService.findBoard(boardId).orElse(null);
        CheckUtil.nullCheck(member);
        CheckUtil.nullCheck(board);
        Comment comment = commentService.postComment(member, board, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReturnSingleCommentForm(comment));
    }

    @Override
    @GetMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<ReturnSingleCommentForm> getComment(@PathVariable Long boardId, @PathVariable Long commentId) {
        Comment comment = commentService.findComment(commentId).orElse(null);
        CheckUtil.nullCheck(comment);
        return ResponseEntity.status(HttpStatus.OK).body(new ReturnSingleCommentForm(comment));
    }

    @Override
    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<ReturnSingleCommentForm> updateComment(HttpServletRequest request,@PathVariable Long boardId, @RequestBody CommentUpdateDto commentUpdateDto, @PathVariable Long commentId, BindingResult bindingResult) {
        jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        String email = jwtUtils.getTokenEmail(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Comment preComment = commentService.findComment(commentId).orElse(null);
        CheckUtil.nullCheck(preComment);
        memberEmailAndCommentEmailSameCheck(email, preComment);
        Comment comment = commentService.updateComment(preComment, commentUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ReturnSingleCommentForm(comment));

    }

    @Override
    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<SuccessReturnForm> deleteComment(HttpServletRequest request,@PathVariable Long boardId, @PathVariable Long commentId) {
        jwtUtils.verifyJwtToken(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        String email = jwtUtils.getTokenEmail(request, env.getProperty("token.ACCESS_HEADER_STRING"));
        Comment comment = commentService.findComment(commentId).orElse(null);
        CheckUtil.nullCheck(comment);
        memberEmailAndCommentEmailSameCheck(email, comment);
        commentService.deleteComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessReturnForm(200));
    }

    @Override
    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<Page<CommentPageDto>> searchCommentPage
            (
                    @PageableDefault(page = 0, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
                    @PathVariable Long boardId,
                    @RequestParam(name = "commentId", required = false) Long commentId
            )
    {
        CommentPageSearchCondition condition = CommentPageSearchCondition
                .builder()
                .commentId(commentId)
                .build();
        Page<CommentPageDto> commentPage = commentService.findCommentPage(pageable, condition, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentPage);
    }

    private static void memberEmailAndCommentEmailSameCheck(String email, Comment comment) {
        if(!email.equals(comment.getMember().getEmail())) {
            throw new RuntimeException("정상적이지 않은 접근");
        }
    }
}
