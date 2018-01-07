package com.feed_the_beast.javacurseforgelib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;
import uk.co.datumedge.hamcrest.json.SameJSONAs;

import static org.junit.Assert.*;

public class BetterEnumAdapterFactoryTestInts {
    private final static Gson GSON;
    private final static String testString = "{\"_0\":0, \"_42\":42, \"_666\":666, \"_min\":-2147483648, \"_max\":2147483647}";
    private final static String testStringFail = "{\"_0\":0, \"_42\":42, \"_666\":666, \"_min\":-2147483649, \"_max\":2147483648}";
    private final static String testStringFailBadValue = "{\"_0\":123456}";

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new BetterEnumAdapterFactory());
        GSON = builder.create();
    }

    private Helper h;

    @Before
    public void setUp() throws Exception {
        h = GSON.fromJson(testString, Helper.class);

    }

    @Test
    public void boxing() throws Exception {
        assertEquals(Integer.class, h._0.getValue().getClass());
        int excepted = 0;
        int actual = h._0.getValue();

        assertSame(excepted, h._0.getValue());
        assertEquals(excepted, h._0.getValue().longValue());
        assertEquals(excepted, h._0.getValue().intValue());
        assertEquals(excepted, actual);
    }

    @Test
    public void deserializer() throws Exception {
        assertTrue(h._0 != null);
        assertTrue(h._none == null);
        assertEquals(BetterEnumInts.NULL, h._0);
        assertEquals(BetterEnumInts.ADAMS, h._42);
    }

    @Test
    public void serializer() throws Exception {
        String s = GSON.toJson(h);
        assertThat(s, SameJSONAs.sameJSONAs(testString));

    }

    @Test(expected = NumberFormatException.class)
    public void shouldFail() throws Exception {
        Helper h = GSON.fromJson(testStringFail, Helper.class);
    }

    @Test(expected = JsonParseException.class)
    public void badValue() throws Exception {
        Helper h = GSON.fromJson(testStringFailBadValue, Helper.class);

    }

    private class Helper {
        private BetterEnumInts _0;
        private BetterEnumInts _42;
        private BetterEnumInts _666;
        private BetterEnumInts _min;
        private BetterEnumInts _max;
        private BetterEnumInts _none;
    }
}
