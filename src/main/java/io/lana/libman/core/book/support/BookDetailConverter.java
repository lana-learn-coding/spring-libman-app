package io.lana.libman.core.book.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@RequiredArgsConstructor
public class BookDetailConverter implements AttributeConverter<BookDetails, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(BookDetails attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookDetails convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) return new SimpleBookDetail();
        try {
            return objectMapper.readValue(dbData, SimpleBookDetail.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
