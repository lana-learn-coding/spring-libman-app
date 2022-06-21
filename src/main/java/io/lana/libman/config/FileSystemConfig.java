package io.lana.libman.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
class FileSystemConfig {
    @Value("${vfs.base-file}")
    private final String baseFile;

    @Bean
    public FileSystemManager fileSystemManager() {
        try {
            final FileSystemManager manager = VFS.getManager();
            if (manager.getBaseFile() == null) {
                final var baseDir = manager.resolveFile(baseFile);
                if (!baseDir.exists()) baseDir.createFolder();
                if (baseDir.exists() && baseDir.isFile())
                    throw new IllegalStateException("Configured file system is not a folder, please check ${vfs.base-file}: " + baseFile);
                ((DefaultFileSystemManager) manager).setBaseFile(baseDir);
            }
            return manager;
        } catch (FileSystemException e) {
            throw new IllegalStateException(e);
        }
    }
}
