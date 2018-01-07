package com.feed_the_beast.javacurseforgelib.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

@Slf4j
public class EnumSetTypeAdapterFactory implements TypeAdapterFactory {
    private final List<Class> allowedEnums = Lists.newArrayList();

    public EnumSetTypeAdapterFactory(List<Class> allowedEnums) {
        this.allowedEnums.addAll(allowedEnums);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        Type type2 = type.getType();
        if (rawType != Set.class || !(type2 instanceof ParameterizedType)) {
            return null;
        }

        Type ret = ((ParameterizedType) type2).getActualTypeArguments()[0];

        if (!allowedEnums.contains(ret)) {
            return null;
        }

        return (TypeAdapter<T>) new EnumSetTypeAdapter((Class)ret);
    }

    private static final class EnumSetTypeAdapter extends TypeAdapter<Set> {
        private Class clazz;

        private EnumSetTypeAdapter(Class clazz) {
            this.clazz = clazz;
        }

        // TODO: modularize static (de)serialize calls
        @Override
        public void write(JsonWriter out, Set value) throws IOException {
            out.value(EnumSetHelpers.serialize(value, clazz));
        }

        @Override
        public Set read(JsonReader in) throws IOException {
            Set result;
            if ( in.peek() == JsonToken.NULL ) {
                in.nextNull();
                return null;
            }

            result = EnumSetHelpers.deserialize(in.nextLong(), clazz);
            return result;
        }
    }
}
