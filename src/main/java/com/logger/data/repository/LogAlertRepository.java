package com.logger.data.repository;

import com.logger.model.LogAlert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAlertRepository extends CrudRepository<LogAlert,Long>  {

    LogAlert findLogAlertByLogId(final String logId);
}
