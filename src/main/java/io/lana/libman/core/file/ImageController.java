package io.lana.libman.core.file;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileSystemManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
@RequestMapping("/files/images")
@RequiredArgsConstructor
class ImageController {
    private final ImageService service;

    private final FileSystemManager fs;

    @GetMapping("{file}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String file) {
        final var id = FilenameUtils.getBaseName(file);
        final var image = service.findOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try (var fileObject = fs.resolveFile(image.getPath());
             var stream = fileObject.getContent().getInputStream()) {
            return ResponseEntity.ok()
                    .header("Content-Type", image.getMimeType())
                    .body(IOUtils.toByteArray(stream));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
