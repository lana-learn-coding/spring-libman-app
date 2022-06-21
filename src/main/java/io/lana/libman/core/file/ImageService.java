package io.lana.libman.core.file;

import io.lana.libman.support.file.FileService;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService extends FileService<Image> {
    Image crop(MultipartFile source, int width, int height);
}
