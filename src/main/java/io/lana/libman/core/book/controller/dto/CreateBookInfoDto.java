package io.lana.libman.core.book.controller.dto;

import io.lana.libman.core.book.BookInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateBookInfoDto extends BookInfo {
    @Min(0)
    @NotNull
    private Integer numberOfBooks = 0;
}
