package com.feed_the_beast.javacurseforgelib.addondumps;

import lombok.Data;

import java.util.List;

@Data
public class MergedDatabase {
    public MergedDatabase () {
    }

    public MergedDatabase (AddonDatabase db) {
        currentDatabase = db;
        oldTimestamp = db.timestamp;
    }

    public AddonDatabase currentDatabase;
    //This isn't set in in all cases
    public AddonDatabase changes = null;
    //This isn't set in in all cases
    public List<DatabaseType> newDBTypes = null;
    public long oldTimestamp;
}
