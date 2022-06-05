package com.logger.model;

import com.logger.data.convertor.DurationConvertor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;

@Entity
@Table(name = "log_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAlert implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String logId;

    @Convert(converter = DurationConvertor.class)
    private Duration duration;

    private boolean alert;

    private String type;

    private String host;
}
