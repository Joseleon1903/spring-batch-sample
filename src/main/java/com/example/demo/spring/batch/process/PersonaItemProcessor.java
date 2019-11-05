package com.example.demo.spring.batch.process;

import com.example.demo.spring.batch.data.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);

    @Override
    public Persona process(Persona persona) throws Exception {
        System.out.println("inside process");
        String name = persona.getNombre().toUpperCase();
        String apellido = persona.getApellido().toUpperCase();
        String dni = "+39 "+ persona.getDni();

        Persona personaOut =  new Persona(name, apellido, dni);

        LOG.info("convert persona : "+ personaOut);

        System.out.println("exiting process");

        return persona;
    }
}
