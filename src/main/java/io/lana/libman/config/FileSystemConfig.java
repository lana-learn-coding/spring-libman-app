package io.lana.libman.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
class FileSystemConfig {

    @Bean
    public FileSystemManager fileSystemManager(ConfigFacade config) {
        try {
            final FileSystemManager manager = VFS.getManager();
            if (manager.getBaseFile() == null) {
                final var baseDir = manager.resolveFile(config.getBaseVfsPath());
                if (!baseDir.exists()) baseDir.createFolder();
                if (baseDir.exists() && baseDir.isFile())
                    throw new IllegalStateException("Configured file system is not a folder, please check ${config.vfs.base-path}: " + config.getBaseVfsPath());
                ((DefaultFileSystemManager) manager).setBaseFile(baseDir);
            }
            return manager;
        } catch (FileSystemException e) {
            throw new IllegalStateException(e);
        }
    }
}
