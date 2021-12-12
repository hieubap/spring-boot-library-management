package vn.isofh.security.configuration.databaseconfig;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:application.properties"})
//@EnableJpaRepositories(
//        basePackages = "library.jpa.entity.main",
//        entityManagerFactoryRef = "userEntityManager",
//        transactionManagerRef = "userTransactionManager")
public class MainDatabaseConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties userDataSource() {
        return new DataSourceProperties();
    }


}