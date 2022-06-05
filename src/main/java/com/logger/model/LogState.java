package com.logger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public enum LogState {
    STARTED("STARTED"), FINISHED("FINISHED");
    private String state;

    private LogState(String state) {
        this.state = state;
    }

    @JsonValue
    public String getState() {
        return this.state;
    }

    @JsonCreator
    public LogState findByState(String state) {
        return Arrays.stream(values())
                .filter(innerState -> innerState.state.equalsIgnoreCase(state))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("LogState::findByState:: Invalid code [%s] ", state)));
    }
}
