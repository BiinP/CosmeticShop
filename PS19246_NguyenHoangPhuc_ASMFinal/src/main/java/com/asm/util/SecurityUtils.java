package com.asm.util;

import org.springframework.security.core.context.SecurityContextHolder;
import com.asm.bean.MyUser;


public class SecurityUtils {
	public static MyUser getPrincipal() {
        return (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
