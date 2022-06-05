package com.logger.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.logger.model.LogModel;
import lombok.SneakyThrows;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public static String writeAsString(Object object) {
        return mapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static LogModel readAsObject(String readStr) {
        return mapper.readValue(readStr, LogModel.class);
    }
}
