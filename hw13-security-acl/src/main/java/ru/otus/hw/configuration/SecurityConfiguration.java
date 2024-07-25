package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                // Настройка страниц и запросов для которых требуется авторизация
                .authorizeHttpRequests((authorize) -> authorize
                        // Разрешаем для стартовой страници
                        .requestMatchers("/").permitAll()
                        // Для всех остальных требуется аутинфикация
                        .requestMatchers("/create/**", "/delete/**", "/edit/**")
                        .hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers("/*").authenticated()
                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}