package com.feed_the_beast.javacurseforgelib.addondumps;

public enum DatabaseType {

    COMPLETE,
    WEEKLY,
    DAILY,
    HOURLY;

    DatabaseType () {
    }

    public String getStringForUrl () {
        return this.toString().toLowerCase();
    }

    /**
     * Gets URL needed to get the timestamp
     */
    public String getTimestampUrl (String game) {
        return Bz2Data.BASE_URL.replace("{}", game) + this.getStringForUrl() + ".json.bz2.txt";
    }

    /**
     * Gets the URL to request the database type at a specific time.
     */
    public String getDownloadUrl (String game, long timestamp) {
        return Bz2Data.BASE_URL.replace("{}", game) + this.getStringForUrl() + ".json.bz2?t=" + timestamp;
    }
}

