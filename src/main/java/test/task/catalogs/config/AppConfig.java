package test.task.catalogs.config;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {
        "test.task.catalogs.repositories",
        "test.task.catalogs.service",
        "test.task.catalogs.mapper"
})
@AllArgsConstructor
public class AppConfig {
    private final Environment env;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(getDataSource());

        Properties properties = new Properties();
        properties.put("hibernate.show-sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

        localSessionFactoryBean.setHibernateProperties(properties);
        localSessionFactoryBean.setPackagesToScan("test.task.catalogs.model");
        return localSessionFactoryBean;
    }
}
