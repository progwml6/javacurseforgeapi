package com.feed_the_beast.javacurseforgelib.utils;

/**
 * see https://support.curse.com/hc/en-us/articles/210356803-Text-Chat-Message-Formatting
 */
public class ChatFormatter {
    //TODO add support for @ tagging users

    public static String bold (String in) {
        return "*" + in + "*";
    }

    public static String italics (String in) {
        return "~" + in + "~";
    }

    /**
     * Adds multiline monospacing -- newlines added automatically
     * @param in String to format
     * @return Formatted string
     */
    public static String mulitlineMonospace (String in) {
        return "```\n" + in + "\n```";
    }

    public static String monospace (String in) {
        return "`" + in + "`";
    }

    public static String strikethrough (String in) {
        return "-" + in + "-";
    }

    public static String tagEveryone () {
        return "@0:everyone";
    }

    public static String tagUser (int id, String username) {
        return "@" + id + ":" + username;
    }

    public static String underscore (String in) {
        return "_" + in + "_";
    }

}
