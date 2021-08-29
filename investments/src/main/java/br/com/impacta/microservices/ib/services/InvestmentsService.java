package br.com.impacta.microservices.ib.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.impacta.microservices.ib.model.TesouroDireto;

public class InvestmentsService {

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("/tesouro-direto.json").getFile());

    @GET
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TesouroDireto> getAllTesouroDireto() throws JsonParseException, JsonMappingException, IOException {

        InputStream inJson = new FileInputStream(file.toString());
        List<TesouroDireto> tesouroDiretos = new ObjectMapper()
            .readValue(inJson, new TypeReference<List<TesouroDireto>>(){});

        // TesouroDireto.persist(tesouroDiretos);
        return tesouroDiretos;
    }

}
