package com.feed_the_beast.javacurseforgelib.examples.app_v1;

import com.feed_the_beast.javacurseforgelib.addondumps.Addon;
import com.feed_the_beast.javacurseforgelib.addondumps.AddonDatabase;
import com.feed_the_beast.javacurseforgelib.addondumps.Bz2Data;
import com.feed_the_beast.javacurseforgelib.addondumps.DatabaseType;
import com.feed_the_beast.javacurseforgelib.addondumps.Filtering;
import com.feed_the_beast.javacurseforgelib.addondumps.MergedDatabase;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import java.util.Date;
import java.util.Optional;

public class Bz2DataTest {
    public static void main (String args[]) {
        Javers javers = JaversBuilder.javers().build();
        //System.out.println(Bz2Data.getDatabaseAsString(Bz2Data.MC_GAME_ID, DatabaseType.HOURLY));
        //System.out.println("\n\n\n\n\n\n\n");
        String stringdb = Bz2Data.getDatabaseAsString(Bz2Data.MC_GAME_ID, DatabaseType.COMPLETE);
        //System.out.println(stringdb);
        System.out.println("sdblen >> " + stringdb.length());
        AddonDatabase db = Bz2Data.getDatabase(Bz2Data.MC_GAME_ID, DatabaseType.COMPLETE);
        System.out.println("NOW >> " + new Date().getTime());
        System.out.println("size >> " + db.data.size() + " time >> " + db.timestamp);
        MergedDatabase latest = Bz2Data.updateCompleteDatabaseIfNeeded(db, Bz2Data.MC_GAME_ID);
        System.out.println("cdb size >> " + latest.currentDatabase.data.size());
        if (latest.changes != null) {
            System.out.println("changes >> " + latest.changes.data.size());
            for (Addon a : latest.changes.data) {
                Optional<Addon> o = Filtering.getAddonById(a.getId(), db);
                if (o.isPresent()) {

                    Addon oa = o.get();
                    if (!oa.equals(a)) {
                        System.out.println(a.getName() + " " + a.getPrimaryAuthorName() + " " + a.getId());
                        System.out.println(a);
                        System.out.println(oa);
                        Diff diff = javers.compare(oa, a);
                        System.out.println(diff);
                    }
                } else {
                    System.out.println(a);
                }
            }
            for (DatabaseType dbt : latest.newDBTypes) {
                System.out.println("updType >> " + dbt.getStringForUrl());
            }
        } else {
            System.out.println("no changes");
        }
        System.out.println("cdb timestamp >> " + latest.currentDatabase.timestamp);
        System.out.println("dbtime diff >> " + (latest.currentDatabase.timestamp - db.timestamp));
        System.out.println("nowdb diff >> " + (new Date().getTime() - latest.currentDatabase.timestamp));
    }
}
