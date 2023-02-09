package com.practice.springbasic.controller.member;

import com.practice.springbasic.controller.member.vo.RequestLoginMemberForm;
import com.practice.springbasic.controller.member.vo.RequestMemberForm;
import com.practice.springbasic.controller.member.vo.ResponseMemberForm;
import com.practice.springbasic.controller.utils.form.SuccessReturnForm;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.utils.check.CommonCheckUtil;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/member-service")
@Validated
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final Environment env;

    public MemberControllerImpl(MemberService memberService, ModelMapper modelMapper, JwtUtils jwtUtils, Environment env) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
        this.env = env;
    }

    @Override
    @PostMapping("/members")
    public ResponseEntity<ResponseMemberForm> joinMember(HttpServletResponse response, @RequestBody RequestMemberForm requestUserForm, BindingResult bindingResult) {
        emailDuplicateCheck(requestUserForm.getEmail());
        nicknameDuplicateCheck(requestUserForm.getNickname());
        MemberDto memberDto = modelMapper.map(requestUserForm, MemberDto.class);

        Member member = memberService.join(memberDto);

        String accessJwtToken = jwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = jwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(env.getProperty("token.ACCESS_HEADER_STRING"), env.getProperty("token.TOKEN_PREFIX") + accessJwtToken);
        response.addHeader(env.getProperty("token.REFRESH_HEADER_STRING"), env.getProperty("token.TOKEN_PREFIX") + refreshJwtToken);

        //id를 좀 더 우아하게 처리해서 mapper로 해버리는 방법은 없을까 고민해보자
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMemberForm);
    }

    @Override
    @PostMapping("/members/login")
    public ResponseEntity<ResponseMemberForm> loginMember(HttpServletResponse response, @RequestBody RequestLoginMemberForm requestLoginMemberForm, BindingResult bindingResult) {
//        CheckUtil.bindingResultCheck(bindingResult.hasErrors());

        Member member =  memberService.findMemberByEmailAndPassword(requestLoginMemberForm.getEmail(), requestLoginMemberForm.getPassword()).orElse(null);
        CommonCheckUtil.nullCheck400(member, "LoginFailedByWrongInput");
//        CommonCheckUtil.nullCheck400(member, ErrorCode.LoginFailedByWrongInput.toString());

        String accessJwtToken = jwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = jwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(env.getProperty("token.ACCESS_HEADER_STRING"), env.getProperty("token.TOKEN_PREFIX") + accessJwtToken);
        response.addHeader(env.getProperty("token.REFRESH_HEADER_STRING"), env.getProperty("token.TOKEN_PREFIX") + refreshJwtToken);

        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @GetMapping("members/{memberId}")
    public ResponseEntity<ResponseMemberForm> getMember(HttpServletRequest request, @PathVariable Long memberId) {
        jwtUtils.verifyJwtTokenAndAuthority(request, memberId, env.getProperty("token.ACCESS_HEADER_STRING"));

        Member member = memberService.findMemberById(memberId).orElse(null);

        CommonCheckUtil.nullCheck404(member, "MemberNotFound");
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @PutMapping("members/{memberId}")
    public ResponseEntity<ResponseMemberForm> updateMember(HttpServletRequest request, @PathVariable Long memberId, @RequestBody RequestMemberForm requestMemberForm, BindingResult bindingResult) {
        //이메일은 바뀌지 말아야 한다.
//        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        jwtUtils.verifyJwtTokenAndAuthority(request, memberId, env.getProperty("token.ACCESS_HEADER_STRING"));
        nicknameDuplicateCheck(requestMemberForm.getNickname());

        MemberDto memberUpdateDto = modelMapper.map(requestMemberForm, MemberDto.class);
        Member member = memberService.findMemberByEmailAndPassword(requestMemberForm.getEmail(), requestMemberForm.getPassword()).orElse(null);
        CommonCheckUtil.nullCheck400(member, "LoginFailedByWrongInput");
        memberService.update(member, memberUpdateDto);
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @DeleteMapping("members/{memberId}")
    public ResponseEntity<SuccessReturnForm>  deleteMember(HttpServletRequest request, @PathVariable Long memberId, @RequestParam String password) {
        jwtUtils.verifyJwtTokenAndAuthority(request, memberId, env.getProperty("token.ACCESS_HEADER_STRING"));

        memberService.withdrawal(memberId, password);
    return ResponseEntity.status(HttpStatus.OK).body(new SuccessReturnForm(200));
    }

    @Override
    @GetMapping("/nickname-check")
    public ResponseEntity<SuccessReturnForm> nicknameDuplicateCheck(
            @RequestParam("nickname") String nickname) {
        boolean result = memberService.duplicateNickname(nickname);
        CommonCheckUtil.duplicateCheck400(result, "DuplicateNickname");
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessReturnForm(200));
    }

    @Override
    @GetMapping("/email-check")
    public ResponseEntity<SuccessReturnForm> emailDuplicateCheck(
            @RequestParam("email") String email) {
        boolean result = memberService.duplicateEmail(email);
        CommonCheckUtil.duplicateCheck400(result, "DuplicateEmail");
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessReturnForm(200));
    }

    public ResponseMemberForm createResponseMemberForm(Member member) {
        return ResponseMemberForm.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
