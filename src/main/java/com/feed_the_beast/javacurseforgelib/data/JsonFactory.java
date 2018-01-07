package com.feed_the_beast.javacurseforgelib.data;

import com.feed_the_beast.javacurseforgelib.utils.BetterEnumAdapterFactory;
import com.feed_the_beast.javacurseforgelib.utils.CurseGUID;
import com.feed_the_beast.javacurseforgelib.utils.CurseGUIDAdapter;
import com.feed_the_beast.javacurseforgelib.utils.DateAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class JsonFactory {
    public static final Gson GSON;
    public static boolean DEBUG = true;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new BetterEnumAdapterFactory());
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(CurseGUID.class, new CurseGUIDAdapter());
        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        builder.enableComplexMapKeySerialization();
        builder.serializeNulls();
        if (DEBUG) {
            builder.setPrettyPrinting();
        }
        GSON = builder.create();
    }

}