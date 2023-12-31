package com.shridharpoman.jwt.api.config;

import com.shridharpoman.jwt.api.filter.CustomAuthenticationFilter;
import com.shridharpoman.jwt.api.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(bCryptPasswordEncoder);

        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
      http.csrf(csrf -> csrf.disable())
               .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authorizeHttpRequests(auth-> auth.requestMatchers("/api/login/**","/api/token/refresh/**").permitAll())
              .authorizeHttpRequests(auth-> auth.requestMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority("ROLE_USER"))
              .authorizeHttpRequests(auth-> auth.requestMatchers(HttpMethod.POST,"/api/user/save/**").hasAnyAuthority("ROLE_ADMIN"))
              .authorizeHttpRequests(auth-> auth.anyRequest().authenticated())
              .authenticationProvider(authenticationProvider())
               .addFilter(customAuthenticationFilter)
              .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager()  {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(authenticationProvider());
        return new ProviderManager(providers);
    }

}
