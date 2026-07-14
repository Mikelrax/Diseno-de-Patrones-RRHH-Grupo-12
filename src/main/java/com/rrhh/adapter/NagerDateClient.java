package com.rrhh.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class NagerDateClient {

    private static final String URL_BASE = "https://date.nager.at/api/v3/PublicHolidays";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<PublicHolidayDTO> obtenerFeriadosPublicos(int anio, String paisIso)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "/" + anio + "/" + paisIso))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Nager.Date respondió con estado " + response.statusCode());
        }

        Type tipoLista = new TypeToken<List<PublicHolidayDTO>>() {
        }.getType();
        return gson.fromJson(response.body(), tipoLista);
    }
}
