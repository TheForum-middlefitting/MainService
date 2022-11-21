package com.practice.springbasic.controller.member;

import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.controller.member.vo.LoginMemberForm;
import com.practice.springbasic.controller.member.vo.RequestMemberForm;
import com.practice.springbasic.controller.member.vo.ResponseMemberForm;
import com.practice.springbasic.controller.utils.check.CheckUtil;
import com.practice.springbasic.controller.utils.form.SuccessResult;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.MemberService;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.utils.chek.CommonCheckUtil;
import com.practice.springbasic.utils.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.practice.springbasic.config.error.ErrorMessage.*;

@RestController
@RequestMapping("/member-service")
@Validated
public class MemberControllerImpl implements MemberController{

    private final MemberService memberService;
    private final ModelMapper modelMapper;

    public MemberControllerImpl(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    @Override
    @PostMapping("/members")
    public ResponseEntity<ResponseMemberForm> joinMember(HttpServletResponse response, @RequestBody RequestMemberForm requestUserForm, BindingResult bindingResult) {
//        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        emailDuplicateCheck(requestUserForm.getEmail());
        nicknameDuplicateCheck(requestUserForm.getNickname());
        MemberDto memberDto = modelMapper.map(requestUserForm, MemberDto.class);

        Member member = memberService.join(memberDto);

        String accessJwtToken = JwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = JwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessJwtToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshJwtToken);

        //id를 좀 더 우아하게 처리해서 mapper로 해버리는 방법은 없을까 고민해보자
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMemberForm);
    }

    @Override
    @PostMapping("/members/login")
    public ResponseEntity<ResponseMemberForm> loginMember(HttpServletResponse response, @RequestBody LoginMemberForm loginMemberForm, BindingResult bindingResult) {
//        CheckUtil.bindingResultCheck(bindingResult.hasErrors());

        Member member =  memberService.findMemberByEmailAndPassword(loginMemberForm.getEmail(), loginMemberForm.getPassword()).orElse(null);
        CommonCheckUtil.nullCheck400(member, LoginFailedByWrongInput);

        String accessJwtToken = JwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = JwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessJwtToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshJwtToken);

        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @GetMapping("members/{memberId}")
    public ResponseEntity<ResponseMemberForm> getMember(HttpServletRequest request, @PathVariable Long memberId) {
        JwtUtils.verifyJwtTokenAndAuthority(request, memberId, JwtProperties.ACCESS_HEADER_STRING);

        Member member = memberService.findMemberById(memberId).orElse(null);

        CommonCheckUtil.nullCheck404(member, MemberNotFound);
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @PutMapping("members/{memberId}")
    public ResponseEntity<ResponseMemberForm> updateMember(HttpServletRequest request, @PathVariable Long memberId, @RequestBody @Validated RequestMemberForm requestMemberForm, BindingResult bindingResult) {
        //이메일은 바뀌지 말아야 한다.
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        JwtUtils.verifyJwtTokenAndAuthority(request, memberId, JwtProperties.ACCESS_HEADER_STRING);
        nicknameDuplicateCheck(requestMemberForm.getNickname());

        Member member = memberService.findMemberByEmailAndPassword(requestMemberForm.getEmail(), requestMemberForm.getPassword()).orElse(null);
        CommonCheckUtil.nullCheck400(member, LoginFailedByWrongInput);
        MemberDto memberUpdateDto = modelMapper.map(requestMemberForm, MemberDto.class);
        memberService.update(member, memberUpdateDto);
        ResponseMemberForm responseMemberForm = createResponseMemberForm(member);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberForm);
    }

    @Override
    @DeleteMapping("members/{memberId}")
    public SuccessResult deleteMember(HttpServletRequest request, @PathVariable Long memberId, @RequestParam String password) {
        JwtUtils.verifyJwtTokenAndAuthority(request, memberId, JwtProperties.ACCESS_HEADER_STRING);

        memberService.withdrawal(memberId, password);

        return new SuccessResult(null);
    }

    @Override
    @GetMapping("/members/nickname-check")
    public SuccessResult nicknameDuplicateCheck(
            @RequestParam("nickname") String nickname) {
        boolean result = memberService.duplicateNickname(nickname);
        CommonCheckUtil.duplicateCheck400(result, DuplicateNickname);
        return new SuccessResult(null);
    }

    @Override
    @GetMapping("/members/email-check")
    public SuccessResult emailDuplicateCheck(
            @RequestParam("email") String email) {
        boolean result = memberService.duplicateEmail(email);
        CommonCheckUtil.duplicateCheck400(result, DuplicateEmail);
        return new SuccessResult(null);
    }

    public ResponseMemberForm createResponseMemberForm(Member member) {
        return ResponseMemberForm.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
