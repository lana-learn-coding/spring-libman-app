package io.lana.libman.core.book.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BatchReturnDto {
    private String query = "";

    private List<String> ids = new ArrayList<>();
}
