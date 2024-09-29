package net.vrakin.medsalary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final InitData initData;

    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, InitData initData) {
        this.userDetailsService = userDetailsService;
        this.initData = initData;
        this.successHandler = new CustomAuthenticationSuccessHandler();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/", "/login", "/register", "/auth-error").permitAll()
                            .requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/security-user").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/security-user").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(successHandler) // Використовуємо кастомний хендлер
                        .failureUrl("/auth-error")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                ).build();
    }

    @Bean
    public CommandLineRunner init() {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                initData.init();
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
