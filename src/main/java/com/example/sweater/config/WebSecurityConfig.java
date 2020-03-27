package com.example.sweater.config;

import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
// Необходимо чтобы заработала анотация @PreAuthorize("hasAuthority('ADMIN')"), необходимая для того чтобы к форме
// редактирования мог обращаться только админ
@EnableGlobalMethodSecurity(prePostEnabled = true)
// Этот класс при старте приложения кофигурирует WebSecurityConfigurerAdapter
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // Позволяет сохранить информацию о пользователе
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        // параметр характеризует надёжность шифрования
        return new BCryptPasswordEncoder(8);
    }
    @Override
    // Система заходит в этот метод передаёт на вход объект в параметр
    protected void configure(HttpSecurity http) throws Exception {
        http
                // и мы в нём включаем авторизацию
                .authorizeRequests()
                    // указываем, что на главную страницу мы разрешаем полный доступ
                    .antMatchers("/", "/registration","/static/**", "/activate/*").permitAll()
                    // для всех остальных запросов требуем авторизацию
                    .anyRequest().authenticated()
                .and()
                    // включаем formLogin
                    .formLogin()
                    // указываем, что loginPage лежит на таком mapping
                    .loginPage("/login")
                    // и даём всем доступ
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
