package com.example.demo.spring.batch.listener;

import com.example.demo.spring.batch.data.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);

    private JdbcTemplate jdbcTemplate;

    public JobListener(JdbcTemplate jdbcTemplate){
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            LOG.info("Finalizo el job validando los resultados");
            jdbcTemplate.query("select nombre, apellido, dni from persona" ,
                    (rs, row ) -> new Persona(rs.getString(1), rs.getString(2),
                            rs.getString(3))).forEach(persona -> {
               LOG.info("Registros: < "+ persona+"> ..");
            });
        }
    }

}
