package io.lana.libman.config;

import org.hibernate.EmptyInterceptor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
class HibernateConfig implements HibernatePropertiesCustomizer {
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", new EmptyInterceptor() {
            // CURRENT_DATE on pgsql does not have parentheses
            // but if we remove the parentheses, hibernate will not recognize the function call for h2
            // (it interpreted as column table.CURRENT_DATE)
            @Override
            public String onPrepareStatement(String sql) {
                String query = super.onPrepareStatement(sql);
                query = query.replace("CURRENT_DATE()", "CURRENT_DATE");
                return query;
            }
        });
    }
}
