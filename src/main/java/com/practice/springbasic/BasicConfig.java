package com.practice.springbasic;

import com.practice.springbasic.repository.MemberJpaRepository;
import com.practice.springbasic.service.MemberService;
import com.practice.springbasic.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfig {
    private final MemberJpaRepository memberRepository;

    @Autowired
    public BasicConfig(MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository);
    }
}
