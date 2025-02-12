package com.mdh.owner.global.security.config;

import com.mdh.owner.global.security.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Profile("!test")
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(antMatcher("/assets/**"), antMatcher("/h2-console/**"), antMatcher("/favicon.ico"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginService loginService) throws Exception {
        var auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(loginService);

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                antMatcher("/api/v1/owner"),
                                antMatcher("/hello"),
                                antMatcher("/restdocs"),
                                antMatcher("/actuator/**"))
                        .permitAll()
                        .anyRequest()
                        .hasRole("OWNER"))
                .anonymous((anonymous) -> anonymous
                        .principal("thisIsAnonymousUser")
                        .authorities("ROLE_ANONYMOUS", "ROLE_UNKNOWN")
                )

                .formLogin((formLogin) -> formLogin
                        .defaultSuccessUrl("/")
                        .usernameParameter("username")
                        .passwordParameter("password")//html 로그인 페이지에 username, pawssword에 해당하는 파라미터 값(아이디랑 비밀번호)
                        .permitAll()
                )

                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("remember-me")
                )
                .rememberMe((rememberMe) -> rememberMe
                        .key("my-remember-me")
                        .rememberMeParameter("remember-me")//html 로그인 페이지에 name에 해당하는 파라미터 값
                        .tokenValiditySeconds(300))

                .sessionManagement((sessionManagement -> sessionManagement
                        .sessionFixation().changeSessionId() // session fixation 방지
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 전략
                        .invalidSessionUrl("/") // 유효하지 않은 세션에서 요청시 리다이렉트 되는 url
                        .maximumSessions(1) // 1개의 로그인만 가능
                        .maxSessionsPreventsLogin(false) // 새로운 로그인 발생시 기존 로그인이 아닌 새로운 로그인 허용
                )).build();
    }
}