package com.feed_the_beast.javacurseforgelib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

public class DateAdapterTest3 {
    private static Gson ownImpl;

    // from ConversationMarkReadNotification
    private static String testString = "{\"date\":\"2016-05-07T12:49:09.334\"}";

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        ownImpl = builder.create();
    }

    private DateAdapterTest3.DateContainingClass c;
    private Date own;


    @Before
    public void setUp() throws Exception {
        c = ownImpl.fromJson(testString, DateAdapterTest3.DateContainingClass.class);
        own = c.date;
    }

    @Ignore
    @Test
    public void debug() {
        System.out.println(own);
    }

    @Test
    public void testMediumSizedDateTimeString() {
        // just test to execute setUp to see if Exception thrown
    }

    private static class DateContainingClass {
        Date date;
    }

}
