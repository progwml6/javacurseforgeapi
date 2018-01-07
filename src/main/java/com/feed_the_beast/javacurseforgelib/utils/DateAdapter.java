package com.feed_the_beast.javacurseforgelib.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

//TODO test and fix this as this is based on Mojang date formats
public class DateAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat curseShort = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//curse date sample short 1970-01-01T00:00:00Z
    private final DateFormat curseMedium = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//curse date sample medium 2011-08-25T21:46:22.773Z
    private final DateFormat curseLong = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");//curse date sample long 2015-05-08T16:53:32.8198336Z

    public DateAdapter () {
        curseShort.setTimeZone(TimeZone.getTimeZone("UTC"));
        curseMedium.setTimeZone(TimeZone.getTimeZone("UTC"));
        curseLong.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public JsonElement serialize (Date value, Type type, JsonSerializationContext context) {
        synchronized (enUsFormat) {
            String ret = this.curseLong.format(value);
            return new JsonPrimitive(ret);
        }
    }

    @Override
    public Date deserialize (JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("Date was not string: " + json);
        }
        if (type != Date.class) {
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + type);
        }
        String value = json.getAsString();
        synchronized (enUsFormat) {
            try {
                return enUsFormat.parse(value);
            } catch (ParseException e) {
                try {
                    return curseLong.parse(value);
                } catch (ParseException e2) {
                    try {
                        return curseMedium.parse(value);
                    } catch (ParseException e3) {
                        try {
                            return curseShort.parse(value);
                        } catch (ParseException e4) {
                            try {
                                if (value.contains("T") && !value.endsWith("Z")) {
                                    value += "Z";
                                    if (value.contains(".")) {
                                        return curseMedium.parse(value);
                                    } else {
                                        return curseShort.parse(value);
                                    }
                                }
                                String tmp = value.replace("Z", "+00:00");
                                return curseLong.parse(value.substring(0, 22) + value.substring(23));
                            } catch (ParseException e5) {
                                throw new JsonSyntaxException("Invalid date: " + value, e3);
                            }
                        }
                    }
                }
            }
        }
    }
}


