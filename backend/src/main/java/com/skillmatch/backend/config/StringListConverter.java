package com.skillmatch.backend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper(); // json mapper

    // convert list to json string
    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(stringList); // serialize list
        } catch (JsonProcessingException e) {
            return null; // return null on error
        }
    }

    // convert json string to list
    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return Arrays.asList(); // return empty list
        }
        try {
            return objectMapper.readValue(
                    s, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
            ); // deserialize json
        } catch (JsonProcessingException e) {
            return Arrays.asList(); // return empty list on error
        }
    }
}
