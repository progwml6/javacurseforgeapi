package com.feed_the_beast.javacurseforgelib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.datumedge.hamcrest.json.SameJSONAs;

import static org.junit.Assert.*;

// TODO: create test enum class for this or use enum which have Longs and make CallType to use Int
public class BetterEnumAdapterFactoryTestLongs {
    private final static Gson GSON;
    private final static String testString =     "{\"_0\":0, \"_42\":42, \"_666\":666, \"_min\":-9223372036854775808, \"_max\":9223372036854775807}";
    private final static String testStringFail = "{\"_0\":0, \"_42\":42, \"_666\":666, \"_min\":-9223372036854775809, \"_max\":9223372036854775808}";

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
        assertEquals(Long.class, h._0.getValue().getClass());
        long excepted = 0;
        long actual = h._0.getValue();

        assertSame(excepted, h._0.getValue());
        assertEquals(excepted, h._0.getValue().intValue());
        assertEquals(excepted, h._0.getValue().longValue());
        assertEquals(excepted, actual);

    }

    @Test
    public void deserializer() throws Exception {
        assertTrue(h._none == null);
        assertTrue(h._0 != null);
        assertEquals(BetterEnumLongs.NULL, h._0);
        assertEquals(BetterEnumLongs.ADAMS, h._42);
    }

    @Test
    public void serializer() throws Exception {
        String s = GSON.toJson(h);
        assertThat(s, SameJSONAs.sameJSONAs(testString));
    }

    @Ignore //Gson bug. https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/stream/JsonReader.java#L976
    @Test(expected = NumberFormatException.class)
    public void shouldFail() throws Exception {
        Helper h = GSON.fromJson(testStringFail, Helper.class);
    }

    private class Helper {
        private BetterEnumLongs _0;
        private BetterEnumLongs _42;
        private BetterEnumLongs _666;
        private BetterEnumLongs _min;
        private BetterEnumLongs _max;
        private BetterEnumLongs _none;
    }
}
