package com.practice.springbasic.controller.member;

import com.practice.springbasic.config.jwt.JwtProperties;
import com.practice.springbasic.controller.form.SuccessCreatedResult;
import com.practice.springbasic.controller.member.vo.RequestMemberForm;
import com.practice.springbasic.controller.member.vo.ResponseMemberForm;
import com.practice.springbasic.service.member.dto.MemberDto;
import com.practice.springbasic.utils.jwt.JwtUtils;
import com.practice.springbasic.controller.member.vo.LoginMemberForm;
import com.practice.springbasic.controller.utils.CheckUtil;
import com.practice.springbasic.controller.form.SuccessResult;
import com.practice.springbasic.domain.member.Member;
import com.practice.springbasic.service.member.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
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
    public ResponseEntity<SuccessCreatedResult> joinMember(HttpServletResponse response, @RequestBody RequestMemberForm requestUserForm, BindingResult bindingResult) {
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        emailDuplicateCheck(requestUserForm.getEmail());
        nicknameDuplicateCheck(requestUserForm.getNickname());
        MemberDto memberDto = modelMapper.map(requestUserForm, MemberDto.class);

        Member member = memberService.join(memberDto);

        String accessJwtToken = JwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = JwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessJwtToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshJwtToken);
        ResponseMemberForm responseMemberForm = createMemberForm(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCreatedResult(responseMemberForm));
    }

    @Override
    @PostMapping("/members/login")
    public SuccessResult loginMember(HttpServletResponse response, @RequestBody LoginMemberForm loginMemberForm, BindingResult bindingResult) {
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        Member member =  memberService.find(loginMemberForm.getEmail(), loginMemberForm.getPassword()).orElse(null);
        memberNullCheck(member);
        assert member != null;

        String accessJwtToken = JwtUtils.generateAccessJwtToken(member.getId(), member.getEmail());
        String refreshJwtToken = JwtUtils.generateRefreshJwtToken(member.getId(), member.getEmail());
        response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessJwtToken);
        response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX + refreshJwtToken);
        return new SuccessResult(createMemberForm(member));
    }

    @Override
    @GetMapping("members/{id}")
    public SuccessResult getMember(HttpServletRequest request, @PathVariable Long id) {
        JwtUtils.verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
        Member member = memberService.findMemberById(id).orElse(null);
        memberInfoNullCheck(member);
        assert member != null;
        return new SuccessResult(createMemberForm(member));
    }

    @Override
    @PutMapping("members/{id}")
    public SuccessResult updateMember(HttpServletRequest request, @PathVariable Long id, @RequestBody @Validated Member member, BindingResult bindingResult) {
        //이메일은 바뀌지 말아야 한다.
        CheckUtil.bindingResultCheck(bindingResult.hasErrors());
        JwtUtils.verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
//        duplicateNicknameCheck(new NicknameCheckForm(member.getNickname()));
        nicknameDuplicateCheck(member.getNickname());

        Member preMember = memberService.find(member.getEmail(), member.getPassword()).orElse(null);
        memberNullCheck(preMember);
        assert preMember != null;

        memberService.update(preMember, createMemberDto(member));
        return new SuccessResult(createMemberForm(preMember));
    }

    @Override
    @DeleteMapping("members/{id}/{password}")
    public SuccessResult deleteMember(HttpServletRequest request, @PathVariable Long id, @PathVariable String password) {
        JwtUtils.verifyJwtToken(request, id, JwtProperties.ACCESS_HEADER_STRING);
        Member compareMember = memberService.findMemberByIdAndPassword(id, password).orElse(null);
        memberNullCheck(compareMember);
        memberService.withdrawal(compareMember);
        return new SuccessResult(null);
    }

    @Override
    @GetMapping("/members/nickname-check")
    public SuccessResult nicknameDuplicateCheck(
            @RequestParam("nickname") String nickname) {
        if(memberService.duplicateNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다");
        }
        return new SuccessResult(null);
    }

    @Override
    @GetMapping("/members/email-check")
    public SuccessResult emailDuplicateCheck(
            @RequestParam("email") String email) {
        if(memberService.duplicateEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다");
        }
        return new SuccessResult(null);
    }

    public void memberNullCheck(Object checkValue) {
        if(checkValue == null) {throw new IllegalArgumentException("이메일 혹은 패스워드가 잘못되었습니다!");}
    }

    public void memberInfoNullCheck(Object checkValue) {
        if(checkValue == null) {throw new IllegalArgumentException("존재하지 않는 사용자입니다!");}
    }

    public MemberDto createMemberDto(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }

    public ResponseMemberForm createMemberForm(Member member) {
        return ResponseMemberForm.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
