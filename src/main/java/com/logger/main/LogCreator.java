package com.logger.main;

import com.google.common.base.Stopwatch;
import com.logger.file.writer.LogWriter;
import com.logger.model.LogModel;
import com.logger.model.LogState;
import com.logger.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.stream.LongStream;

@Slf4j
public class LogCreator {

    public static void main(String[] args) {
        //        final long numberLogs = 70000000 + 1;
        final long numberLogs=70000+1;
        final long flushAfterEvery = 50000;
        final String taskName = "FileWrite";
        final long notifier = 100000;
        Stopwatch totalLogCreationWatcher = Stopwatch.createStarted();
        LocalDateTime localDateTime = LocalDateTime.now();
        LogWriter<LogModel> logWriter = new LogWriter<>("E://log.txt");
        CompletableFuture future1 = CompletableFuture.runAsync(() -> {
            LongStream.range(1, numberLogs).boxed()
                    .forEach(i -> {
                        LogModel logModel = new LogModel(String.format("LOGID_%08d", i), LogState.STARTED, localDateTime);
                        logWriter.writeObject(logModel);
//                    if(i%notifier==0){
//                        log.debug("LogCreator::main::STARTED:: done so far [{}]",i);
//                    }
                    });
        });

        CompletableFuture future2 = CompletableFuture.runAsync(() -> {
            LongStream.range(1, numberLogs).boxed()
                    .forEach(i -> {
                        LocalDateTime dateTime = localDateTime.plus(i % 2 == 0 ? 5 : 3, ChronoUnit.NANOS);
                        LogModel logModel = new LogModel(String.format("LOGID_%08d", i), LogState.FINISHED, dateTime);
                        logWriter.writeObject(logModel);
                        if (i % notifier == 0) {
                            log.debug("LogCreator::main::FINISHED:: done so far [{}]", i);
                        }
                        if (i % flushAfterEvery == 0) {
                            logWriter.flushStream();
                            log.debug("LogCreator::main::FLUSH Stream:: done so far [{}]", i);
                        }
                    });
        });
        future1.join();
        future2.join();
        try {
            logWriter.closeResource();
        } catch (IOException e) {
            log.error("Error while closing resources. ", e);
        }
        log.debug("LogCreator::main::Time total:: [{}]", totalLogCreationWatcher.elapsed());
        totalLogCreationWatcher.stop();
    }
}
