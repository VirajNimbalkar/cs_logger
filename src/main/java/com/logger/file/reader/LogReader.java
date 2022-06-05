package com.logger.file.reader;

import com.logger.model.LogModel;
import com.logger.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class LogReader<T> {

    FileReader fileReader;

    BufferedReader bufferedReader;

    @SneakyThrows
    public LogReader(final String pathToFile) {
        fileReader = new FileReader(pathToFile);
        bufferedReader = new BufferedReader(fileReader);

    }

    public Flux<LogModel> readFile() {
        int logRecord = 1000000;

        return Flux.create((FluxSink<String> sink) -> {
                    try {
                        String line;
                        long lineNo = 0;
                        while ((line = bufferedReader.readLine()) != null) {
                            sink.next(line);
                            lineNo++;
                            if (lineNo % logRecord == 0) {
                                log.debug("LogReader::readFile:: set completed [{}]", lineNo);
                            }
                        }
                        log.debug("LogReader::readFile::going to complete::");
                        sink.complete();
                    } catch (IOException e) {
                        log.error("LogReader::readFile::going to complete::", e);
                        sink.error(e);
                    }
                })
                .map(JsonUtils::readAsObject)
                .doFinally((finals) -> {
                    log.debug("LogReader::readFile::doFinally::");
                    this.closeResource();
                });

    }

    @SneakyThrows
    private void closeResource() {
        if (Objects.nonNull(bufferedReader)) {
            bufferedReader.close();
        }

        if (Objects.nonNull(fileReader)) {
            fileReader.close();
        }
    }
}
