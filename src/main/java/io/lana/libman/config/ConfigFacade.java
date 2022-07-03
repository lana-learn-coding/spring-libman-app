package io.lana.libman.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ConfigFacade {
    @Value("${config.vfs.base-path}")
    private final String baseFile;

    @Value("${config.cost.overdue-default}")
    private final double cost;

    @Value("${config.email.from}")
    private final String from;

    @Value(value = "${config.app.url}")
    private final String appUrl;

    public String getBaseVfsPath() {
        return baseFile;
    }

    public double getOverDueDefaultCost() {
        return cost;
    }

    public String getEmailFrom() {
        return from;
    }

    public String getAppUrl() {
        return appUrl;
    }

}
