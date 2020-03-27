package com.example.sweater.domain;

import org.springframework.security.core.GrantedAuthority;

// Значения enum(USER) являются имплементацией GrantedAuthority
public enum Role implements GrantedAuthority{
    USER,ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
