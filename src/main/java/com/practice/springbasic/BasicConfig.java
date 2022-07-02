package com.practice.springbasic;

import com.practice.springbasic.repository.MemberRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfig {
    private final MemberRepositoryCustom memberRepositoryCustom;

    @Autowired
    public BasicConfig(MemberRepositoryCustom memberRepositoryCustom) {
        this.memberRepositoryCustom = memberRepositoryCustom;
    }
}
