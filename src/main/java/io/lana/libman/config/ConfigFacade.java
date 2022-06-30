package io.lana.libman.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConfigFacade {
    @Value("${config.vfs.base-path}")
    private final String baseFile;

    @Value("${config.cost.overdue-default}")
    private final double cost;

    public String getBaseVfsPath() {
        return baseFile;
    }

    public double getOverDueDefaultCost() {
        return cost;
    }
}