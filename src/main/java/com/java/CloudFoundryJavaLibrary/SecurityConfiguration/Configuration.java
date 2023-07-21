package com.java.CloudFoundryJavaLibrary.SecurityConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@org.springframework.context.annotation.Configuration
public class Configuration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() 
            .authorizeRequests()
                .antMatchers("/**").permitAll() 
                .anyRequest().authenticated()
            .and()
            .formLogin();
    }
    
}

