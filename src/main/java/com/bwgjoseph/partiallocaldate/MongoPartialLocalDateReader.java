package com.bwgjoseph.partiallocaldate;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

public class MongoPartialLocalDateReader implements Converter<Document, PartialLocalDate> {

    @Override
    public PartialLocalDate convert(Document source) {
        return new PartialLocalDate(source.getString(MongoPartialLocalDateWriter.DATE_FIELD));
    }

}
