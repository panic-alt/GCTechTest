package com.gc.services.subscription.configuration;


import com.gc.services.subscription.utils.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

   private final JwtAuthenticationFilter jwtAuthenticationFilter;
   private final AuthenticationProvider authenticationProvider;
   private static final String[] AUTH_WHITELIST = {
           "/v2/api-docs",
           "/swagger-resources/**",
           "/swagger-ui.html",
           "/webjars/**",
           "/v3/api-docs/**",
           "/swagger-ui/**"
   };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http.csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(authRequest -> authRequest
                       .requestMatchers(AUTH_WHITELIST).permitAll()
                       .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                       .requestMatchers("/auth/**").permitAll()
                       .anyRequest().authenticated()
               ).sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
       http.headers(headers ->
               headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
       return http.build();
   }

}
