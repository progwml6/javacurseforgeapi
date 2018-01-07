package com.feed_the_beast.javacurseforgelib.utils;

/*
 * Adapted from https://github.com/google/gson/blob/3361030766593c0b05e17dace401cce6fcdf9e24/gson/src/main/java/com/google/gson/internal/bind/TypeAdapters.java#L719
 */

/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * (de)serialize from/to values, not with enum names like GSON's default adapter does
 *
 * Ordinal values start from zero and are continuous
 */

public class BetterEnumAdapterFactory implements TypeAdapterFactory {
        //@SuppressWarnings({"rawtypes", "unchecked"})
        @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass(); // handle anonymous subclasses
            }
            // We only care enums which implements BetterEnum for getValue method
            if (!BetterEnum.class.isAssignableFrom(rawType) || rawType == BetterEnum.class) {
                return null;
            }
            return (TypeAdapter<T>) new EnumTypeAdapter(rawType);
        }

        private static final class EnumTypeAdapter<T extends Enum<T> & BetterEnum<S>, S extends Number> extends TypeAdapter<T> {
            private final Map<S, T> valueToConstant = new HashMap<>();
            private final Map<T, S> constantToValue = new HashMap<>();
            private boolean valueIsLong = false;


        public EnumTypeAdapter(Class<T> classOfT) {
            for (T constant : classOfT.getEnumConstants()) {
                S value = constant.getValue();

                valueToConstant.put(value, constant);
                constantToValue.put(constant, value);
            }

            // stupid hack
            for (S n: valueToConstant.keySet()) {
                if (n instanceof Long) {
                    valueIsLong = true;
                }
                return;
            }
        }
        @Override public T read(JsonReader in) throws IOException {
            T result;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            // T
            if (valueIsLong) {
                result = valueToConstant.get(in.nextLong());
            } else {
                result = valueToConstant.get(in.nextInt());
            }

            if (result == null) {
                throw new JsonParseException("Failed to find enum");
            }

            return result;
        }

        @Override public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : constantToValue.get(value));
        }
    }
}
