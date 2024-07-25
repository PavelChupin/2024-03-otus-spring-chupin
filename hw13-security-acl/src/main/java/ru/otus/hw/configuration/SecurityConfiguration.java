package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
                        .requestMatchers("/").permitAll()
                        // Разрешаем для стартовой страници
                        //.requestMatchers("/").permitAll()
                        // Для всех остальных требуется аутинфикация
                        .requestMatchers("/create/**", "/delete/**", "/edit/**").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers("/*").authenticated()
                        .anyRequest().denyAll()
                )
                //.anonymous(a -> a.principal(new AnonimusUD()).authorities("ROLE_ANONYMOUS"))
                //.addFilterAfter(new MyOwnFilter(), AuthorizationFilter.class)
                // Аунтификация через BASE аунтификацию логин и пароль передается в Header в параметре
                // Authorization в кодировке BASE64 (login:pass)
                //.httpBasic(Customizer.withDefaults());
                // Аунтификация через форму логина и пароля form-base
                // Оставаться на той же странице куда выполнили переход и перед которой потребовалась аутинфикация
                .formLogin(Customizer.withDefaults());
                // Переходы на нужные страницы в случаии успешной или неуспешной аутинфикации
                //.formLogin(fm -> fm.defaultSuccessUrl("/success").failureForwardUrl("/fail"));
                // На какой период времени запоминать аунтификацию
                //.rememberMe(rm -> rm.key("AnyKey").tokenValiditySeconds(600));
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}