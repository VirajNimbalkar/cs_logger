package com.logger.data.repository;

import com.logger.config.DataConfig;
import com.logger.data.config.DataTestConfig;
import com.logger.model.LogAlert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@ContextConfiguration(classes = DataTestConfig.class)
@ActiveProfiles("test")
@ComponentScan(basePackages = {"com.logger"})
public class LogAlertRepositoryTest {

    @Autowired
    private LogAlertRepository logAlertRepository;

    static LogAlert logAlert;
    @BeforeAll
    static void setup() {
        log.info("@BeforeAll - executes once before all test methods in this class");
        logAlert=new LogAlert();
        logAlert.setLogId("LOG_ID_000000001");
        logAlert.setDuration(Duration.ofMillis(7));
        logAlert.setAlert(true);
        logAlert.setHost("LOCALHOST");
        logAlert.setType("APPLICATION_LOG");
    }

    @Test
    public void whenFindingLogModelById_thenCorrect() {
        logAlertRepository.save(logAlert);
        assertSame(logAlert.getClass(),logAlertRepository.findLogAlertByLogId(logAlert.getLogId()).getClass());
    }
}
