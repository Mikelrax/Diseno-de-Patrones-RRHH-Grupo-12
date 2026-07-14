package com.rrhh.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ExportadorJSON {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString());
                    }
                }

                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    return LocalDate.parse(in.nextString());
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString());
                    }
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    return LocalDateTime.parse(in.nextString());
                }
            })
            .create();

    public <T> void exportar(T datos, Path destino) throws IOException {
        Files.writeString(destino, gson.toJson(datos));
    }

    public <T> String generarJson(T datos) {
        return gson.toJson(datos);
    }
}
