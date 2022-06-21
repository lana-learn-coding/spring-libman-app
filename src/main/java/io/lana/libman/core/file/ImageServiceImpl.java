package io.lana.libman.core.file;

import io.lana.libman.support.file.FileViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystemManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ImageServiceImpl implements ImageService {
    private static final String[] supportedType = {"image/jpeg", "image/png"};

    private static final long maxSize = 3_000_000;

    private final ImageRepo repo;

    private final FileSystemManager fs;

    @Override
    public boolean validate(MultipartFile file) {
        if (file == null || file.isEmpty()) return true;
        if (file.getSize() > maxSize) throw new FileViolationException(file, "File size too large");

        final var contentType = file.getContentType();
        for (String type : supportedType) {
            if (StringUtils.equalsIgnoreCase(type, contentType)) return true;
        }
        throw new FileViolationException(file, "File type not supported");
    }

    @Override
    public Image save(Image file) {
        file.getSource().ifPresent(source -> {
            try (final var fileObject = fs.resolveFile(file.getPath())) {
                fileObject.createFile();
                try (final var output = fileObject.getContent().getOutputStream();
                     final var input = source.getInputStream()) {
                    input.transferTo(output);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        repo.save(file);
        return file;
    }

    @Override
    public Optional<Image> findOne(String id) {
        return repo.findById(id);
    }

    @Override
    public Image createFrom(MultipartFile file) {
        final var image = new Image(file);
        image.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
        image.setMimeType(file.getContentType());
        return image;
    }

    @Override
    public Image crop(MultipartFile source, int width, int height) {
        try (final var input = source.getInputStream()) {
            final var bf = scaleToCropIfNeeded(ImageIO.read(input), width, height);
            if (bf.getWidth() <= width && bf.getHeight() <= height) return createFrom(source);

            final var wCropStart = (bf.getWidth() - width) / 2;
            final var hCropStart = (bf.getHeight() - height) / 2;
            final var cropped = bf.getSubimage(wCropStart, hCropStart, width, height);
            final var image = new Image(() -> {
                final var output = new ByteArrayOutputStream();
                final var formatName = StringUtils.removeStart(source.getContentType(), "image/");
                ImageIO.write(cropped, Objects.requireNonNull(formatName), output);
                return new ByteArrayInputStream(output.toByteArray());
            });
            image.setExtension(FilenameUtils.getExtension(source.getOriginalFilename()));
            image.setMimeType(source.getContentType());
            return image;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private BufferedImage scaleToCropIfNeeded(BufferedImage image, int width, int height) {
        final var wRatio = Math.floor((double) image.getWidth() / width);
        final var hRatio = Math.floor((double) image.getHeight() / height);
        final var ratio = Math.min(wRatio, hRatio);
        if (ratio < 1) {
            final var scaledWidth = (int) Math.ceil(image.getWidth() / ratio);
            final var scaledHeight = (int) Math.ceil(image.getHeight() / ratio);
            return scaleImage(image, scaledWidth, scaledHeight);
        }

        if (ratio > 1.5) {
            final var reduceRatio = 1.2 / ratio;
            final var scaledWidth = (int) Math.ceil(image.getWidth() * reduceRatio);
            final var scaledHeight = (int) Math.ceil(image.getHeight() * reduceRatio);
            return scaleImage(image, scaledWidth, scaledHeight);
        }
        return image;
    }

    private BufferedImage scaleImage(BufferedImage image, int scaledWidth, int scaledHeight) {
        final var outputImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        final var g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return outputImage;
    }
}
