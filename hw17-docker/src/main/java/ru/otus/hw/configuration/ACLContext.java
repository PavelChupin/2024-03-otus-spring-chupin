package ru.otus.hw.configuration;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;

import javax.sql.DataSource;

@Configuration
//@EnableAutoConfiguration
// Включить безопасность методов для поддержки аннотаций @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ACLContext {

    @Bean
    public DefaultPermissionFactory permissionFactory() {
        return new DefaultPermissionFactory();
        // Использование собственных разрешений
        //return new DefaultPermissionFactory(FlipTablePermission.class);
    }

    @Bean
    // Стратегия авторизации в ACL определяет, кто и как может изменять ACL глобально
    // В данной ситуации полные права предоставляются пользователям с правом ROLE_LIBRARIAN
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));
    }

    @Bean
    // Логгер аудита (да, System.out)
    public ConsoleAuditLogger consoleAuditLogger() {
        return new ConsoleAuditLogger();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy(ConsoleAuditLogger consoleAuditLogger) {
        return new DefaultPermissionGrantingStrategy(consoleAuditLogger);
    }

    @Bean
    // Кэш ACL
    public SpringCacheBasedAclCache aclCache(PermissionGrantingStrategy permissionGrantingStrategy,
                                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        final ConcurrentMapCache aclCache = new ConcurrentMapCache("acl_cache");
        return new SpringCacheBasedAclCache(aclCache, permissionGrantingStrategy, aclAuthorizationStrategy);
    }


    @Bean
    // Стратегия поиска ACL
    public LookupStrategy lookupStrategy(DataSource dataSource,
                                         SpringCacheBasedAclCache aclCache,
                                         AclAuthorizationStrategy aclAuthorizationStrategy,
                                         ConsoleAuditLogger consoleAuditLogger) {
        BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource,
                aclCache, aclAuthorizationStrategy, consoleAuditLogger);
        // Принудительное использование информации о типе идентификаторов сущностей из БД
        lookupStrategy.setAclClassIdSupported(true);
        //lookupStrategy.setPermissionFactory(permissionFactory);

        return lookupStrategy;
    }

    @Bean
    // Собственно ACL-сервис
    public MutableAclService aclService(DataSource dataSource,
                                        LookupStrategy lookupStrategy,
                                        SpringCacheBasedAclCache aclCache) {
        // Собственно ACL-сервис
        JdbcMutableAclService mutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);

        // Запрос на получение идентификатора созданной записи в acl_class
        mutableAclService.setClassIdentityQuery("select currval('acl_class_id_seq')");
        // Запрос на получение идентификатора созданной записи в acl_sid
        mutableAclService.setSidIdentityQuery("select currval('acl_sid_id_seq')");
        // Принудительное использование информации о типе идентификаторов сущностей из БД
        mutableAclService.setAclClassIdSupported(true);

        return mutableAclService;
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService) {
        // Компонент для определения прав в SPEL взамен стандартному DenyAppPermissionEvaluator
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService);
        //aclPermissionEvaluator.setPermissionFactory(permissionFactory);
        return aclPermissionEvaluator;
    }

//    @Bean
//    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(
//            MutableAclService aclService,
//            AclPermissionEvaluator permissionEvaluator) {
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        expressionHandler.setPermissionEvaluator(permissionEvaluator);
//        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService));
//        return expressionHandler;
//    }

    @Bean
    // Handler работающий с методами. Он сам внедряется к контект спринга и нужные компоненты.
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(
            AclPermissionEvaluator permissionEvaluator,
            ApplicationContext applicationContext) {
        // Компонент для обработки SPEL-выражений для безопасности методов
        // В отличие от DefaultHttpSecurityExpressionHandler он внедряется сам в зависимые объекты
        var defaultHttpSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }

    @Bean
    // Handler работающий в http фильтрах
    public DefaultHttpSecurityExpressionHandler httpSecurityExpressionHandler(
            AclPermissionEvaluator permissionEvaluator,
            ApplicationContext applicationContext) {
        // Компонент для обработки SPEL-выражений для HTTP-безопасности
        var defaultHttpSecurityExpressionHandler = new DefaultHttpSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }

}