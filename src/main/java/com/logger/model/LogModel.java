package com.logger.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogModel {

    private final String id;
    private final LogState state;
    private final LocalDateTime timestamp;

    public LogModel() {
        id="NA";
        state=LogState.STARTED;
        timestamp=LocalDateTime.now();
    }
    public LogModel(String ids,LogState states,LocalDateTime time) {
        id=ids;
        state=states;
        timestamp=time;
    }

    private Long type;
    private String host;
}
