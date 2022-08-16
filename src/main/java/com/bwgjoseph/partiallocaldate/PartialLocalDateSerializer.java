package com.bwgjoseph.partiallocaldate;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * PartialLocalDate object to JSON
 */
@JsonComponent
public class PartialLocalDateSerializer extends JsonSerializer<PartialLocalDate> {

    @Override
    public void serialize(PartialLocalDate value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.toString());
    }

}
