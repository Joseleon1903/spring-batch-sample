package com.example.demo.spring.batch;

import com.example.demo.spring.batch.data.Persona;
import com.example.demo.spring.batch.listener.JobListener;
import com.example.demo.spring.batch.process.PersonaItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Persona> reader(){
        return new FlatFileItemReaderBuilder<Persona>()
                .name("personaItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[] {"nombre", "apellido", "dni"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>(){{
                    setTargetType(Persona.class);
                }}).build();
    }

    @Bean
    public PersonaItemProcessor processor(){
        return new PersonaItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Persona> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Persona>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into persona (nombre, apellido, dni) values(:nombre, :apellido, :dni)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importPersonaJob(JobListener listener, Step step){
        return jobBuilderFactory.get("importPersonaJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Persona> writer){
        return stepBuilderFactory.get("step1")
                .<Persona, Persona> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

}
