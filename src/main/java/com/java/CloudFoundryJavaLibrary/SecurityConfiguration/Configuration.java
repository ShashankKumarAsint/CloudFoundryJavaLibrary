package com.java.CloudFoundryJavaLibrary.SecurityConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@org.springframework.context.annotation.Configuration
public class Configuration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF protection for all endpoints
            .authorizeRequests()
                .antMatchers("/**").permitAll() // Permit all requests to /public endpoints
                .anyRequest().authenticated() // Require authentication for all other endpoints
            .and()
            .formLogin();
    }
}

