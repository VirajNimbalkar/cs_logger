package com.logger.main;

import com.google.common.base.Stopwatch;
import com.logger.data.repository.LogAlertRepository;
import com.logger.file.reader.LogReader;
import com.logger.model.LogAlert;
import com.logger.model.LogModel;
import com.logger.model.LogState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.util.Pair;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@SpringBootApplication
@ComponentScan("com.logger")
public class LogAnalyzer implements ApplicationRunner {

    @Autowired
    LogAlertRepository logAlertRepository;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(LogAnalyzer.class);
//        Thread.sleep(Duration.ofMinutes(5).toMillis());
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stopwatch totalLogCreationWatcher = Stopwatch.createStarted();
        log.debug("LogAnalyzer::run::Spring Application for Log Analyzer started.");

        startAnalyzingLogs();

        log.debug("LogAnalyzer::run::Spring Application for Log Analyzer completed.");
        log.debug("LogAnalyzer::main::Time total:: [{}]", totalLogCreationWatcher.elapsed());
        totalLogCreationWatcher.stop();

    }

    private void startAnalyzingLogs() throws InterruptedException {
        LogReader<LogModel> logReader = new LogReader<>("E://log.txt");
        ConcurrentMap<String, Set<LogModel>> records = new ConcurrentHashMap<>();
        Disposable disposable = logReader.readFile()
                .map(logModel -> {
                    records.computeIfAbsent(logModel.getId(), lm -> new HashSet<LogModel>())
                            .add(logModel);
                    records.computeIfPresent(logModel.getId(), (T, R) -> {
                        R.add(logModel);
                        return R;
                    });

                    return Pair.of(logModel.getId(), records.get(logModel.getId()));
                })
                .filter(pair -> pair.getSecond().size() == 2)
                // .log()
                .metrics()
                .switchMap((pair -> {
                    Set<LogModel> modelSet = pair.getSecond();

                    LogModel startedModel = modelSet.stream().filter(model -> model.getState().compareTo(LogState.STARTED) == 0).findFirst().get();
                    LogModel finishModel = modelSet.stream().filter(model -> model.getState().compareTo(LogState.FINISHED) == 0).findFirst().get();
                    long diff = ChronoUnit.NANOS.between(startedModel.getTimestamp(), finishModel.getTimestamp());
                    records.remove(startedModel.getId());// Remove Objects from Memory, deference  the set from map
                    LogAlert logAlert = null;
                    if (diff > 4) {
                        logAlert = new LogAlert();
                        logAlert.setLogId(logAlert.getLogId());
                        logAlert.setDuration(Duration.ofMillis(diff));
                        logAlert.setAlert(true);
                        logAlert.setHost(logAlert.getHost());
                        logAlert.setType(logAlert.getType());

                    } else {
                        // Do nothing as of now
                    }
                    return Mono.justOrEmpty(logAlert);

                }))
                .filter(Objects::nonNull)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe((logAlert) -> {
                    saveLogAlerts(logAlert);
                });

        while (!disposable.isDisposed()) {
            Thread.sleep(Duration.ofSeconds(10).toMillis());
        }
    }

    private void saveLogAlerts(LogAlert logAlert) {
        logAlertRepository.save(logAlert);
    }
}
