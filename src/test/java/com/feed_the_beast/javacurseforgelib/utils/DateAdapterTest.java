package com.feed_the_beast.javacurseforgelib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.co.datumedge.hamcrest.json.SameJSONAs;

import static org.junit.Assert.*;

import java.util.Date;

public class DateAdapterTest {
    private static Gson gsonImpl;
    private static Gson ownImpl;

    // only short ISO8601 is supported by Gson's builtin adapters
    private static String testString = "{\"date\":\"0001-01-01T00:00:00Z\"}";

    static {
        GsonBuilder builder = new GsonBuilder();
        gsonImpl = builder.create();

        builder.registerTypeAdapter(Date.class, new DateAdapter());
        ownImpl = builder.create();
    }

    private DateContainingClass c;
    private Date own;
    private Date gson;

    @Before
    public void setUp() throws Exception {
        c = ownImpl.fromJson(testString, DateContainingClass.class);
        own = c.date;
        gson = gsonImpl.fromJson(testString, DateContainingClass.class).date;
    }

    @Ignore
    @Test
    public void debug() throws Exception {
        System.out.println(own.getTime() - gson.getTime());
        System.out.println(ownImpl.toJson(c));
        System.out.println(own);
        System.out.println(gson);
    }

    @Test
    public void dateEquals() throws Exception {
        assertTrue( own.equals(gson) );
    }

    @Ignore
    @Test
    public void output() {
        assertEquals(testString, ownImpl.toJson(c));
    }

    @Ignore
    // Better libraries for testing this?
    @Test
    public void output2() {
        assertThat(ownImpl.toJson(c), SameJSONAs.sameJSONAs(testString));
    }

    private static class DateContainingClass {
        Date date;
    }
}
