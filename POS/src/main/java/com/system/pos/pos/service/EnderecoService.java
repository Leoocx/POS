package com.system.pos.pos.service;

import com.system.pos.pos.model.Endereco;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EnderecoService {

    public Endereco buscarEnderecoPorCep(String cep) throws Exception {
        if (cep == null || cep.isEmpty() || !cep.matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("CEP inválido");
        }

        cep = cep.replace("-", "");
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            Endereco endereco = mapper.readValue(response.body(), Endereco.class);

            if (endereco.getCep() == null) {
                throw new Exception("CEP não encontrado");
            }

            return endereco;
        } else {
            throw new Exception("Erro ao consultar CEP: " + response.statusCode());
        }
    }
}