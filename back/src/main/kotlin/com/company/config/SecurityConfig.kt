package com.company.config

import com.company.model.member.OAuth2MemberUserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oauth2MemberUserService: OAuth2MemberUserService,
    @Value("\${app.oauth2.success-redirect-url}") private val successRedirectUrl: String
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/me", "/auth/logout").permitAll()
                    .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().permitAll()
            }
            .oauth2Login { oauth ->
                oauth
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(oauth2MemberUserService)
                    }
                    .defaultSuccessUrl(successRedirectUrl, true)
            }
            .logout { logout ->
                logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl(successRedirectUrl)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }

        return http.build()
    }
}
