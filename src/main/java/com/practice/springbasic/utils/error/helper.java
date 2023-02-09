package com.practice.springbasic.utils.error;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

public class helper {
    //@Component
    @Configuration
    public static class ErrorHelper {
        private final Environment env;

        public ErrorHelper(Environment env) {
            this.env = env;
        }

        public String getCode(String Error) {
            try {
                return String.format(Objects.requireNonNull(env.getProperty(Error + ".code")));
            }
            catch (NullPointerException e) {
                return String.format(Objects.requireNonNull(env.getProperty("Unexpected.code")));
            }
        }

        public String getMsg(String Error) {
            try {
                return String.format(Objects.requireNonNull(env.getProperty("Unexpected.msg")));
            }
            catch (NullPointerException e) {
                return "0";
            }
        }
    }
}
