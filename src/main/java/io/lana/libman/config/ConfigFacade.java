package io.lana.libman.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConfigFacade {
    @Value("${config.vfs.base-path}")
    private final String baseFile;

    @Value("${config.cost.default}")
    private final double cost;

    @Value("${config.cost.overdue-multiply}")
    private final double overDueMultiply;

    public String getBaseVfsPath() {
        return baseFile;
    }

    public double getDefaultCost() {
        return cost;
    }

    public double getDefaultOverDueMultiply() {
        return overDueMultiply;
    }
}
