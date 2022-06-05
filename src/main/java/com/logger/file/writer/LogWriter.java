package com.logger.file.writer;

import com.logger.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class LogWriter<T> {
    private FileWriter file;
    private BufferedWriter bufferedWriter;

    @SneakyThrows
    public LogWriter(String path) {
        file = new FileWriter(path, true);
        bufferedWriter = new BufferedWriter(file);
    }

    @SneakyThrows
    public void writeObject(T logModel) {
        bufferedWriter.append(JsonUtils.writeAsString(logModel) + "\n");
    }

    @SneakyThrows
    public void flushStream() {
        bufferedWriter.flush();
    }

    public void closeResource() throws IOException {
        if (Objects.nonNull(bufferedWriter)) {
            bufferedWriter.close();
        }
        if (Objects.nonNull(file)) {
            file.close();
        }
    }
}
