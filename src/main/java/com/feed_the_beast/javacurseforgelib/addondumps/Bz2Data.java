package com.feed_the_beast.javacurseforgelib.addondumps;

import com.feed_the_beast.javacurseforgelib.data.JsonFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

@Slf4j
public class Bz2Data {
    public static final String BASE_URL = "http://clientupdate-v6.cursecdn.com/feed/addons/{}/v10/";
    public static final String MC_GAME_ID = "432";
    private static final OkHttpClient client = new OkHttpClient();
    public static boolean debug = false;

    public static long getTimestamp (String game, DatabaseType type) {
        return Long.parseLong(getNetworkData(type.getTimestampUrl(game)));
    }

    public static String getNetworkData (String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ioe) {
            log.error("error downloading curse timestamp", ioe);
        }

        return null;
    }

    public static String getDatabaseAsString (String game, DatabaseType type) {
        return getAndDecompressBz2(type.getDownloadUrl(game, getTimestamp(game, type)));
    }

    /**
     * gets Addon Database from curse
     * @param game gameId for database
     * @param type type of database
     * @return AddonDatabase of data for that game
     */
    public static AddonDatabase getDatabase (String game, DatabaseType type) {
        return getObjectFromData(getAndDecompressBz2(type.getDownloadUrl(game, getTimestamp(game, type))));
    }

    public static AddonDatabase getInitialDatabase (@Nonnull String game) {
        AddonDatabase db = getDatabase(game, DatabaseType.COMPLETE);
        MergedDatabase mdb = updateCompleteDatabaseIfNeeded(db, game);
        return mdb.currentDatabase;
    }

    public static AddonDatabase getObjectFromData (String data) {
        return JsonFactory.GSON.fromJson(data, AddonDatabase.class);
    }

    public static String getAndDecompressBz2 (String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            BZip2CompressorInputStream cis = new BZip2CompressorInputStream(response.body().byteStream());
            String ret = IOUtils.toString(cis, "UTF-8");
            cis.close();
            return ret;
        } catch (IOException ioe) {
            log.error("error downloading curse bz2", ioe);
        }
        return null;
    }

    public static MergedDatabase mergeDumps (@Nonnull MergedDatabase main, @Nonnull AddonDatabase newDump, boolean forceMerge) {
        MergedDatabase db = mergeDumps(main.currentDatabase, newDump, forceMerge);
        if (main.changes != null && !main.changes.data.isEmpty() && !main.changes.data.isEmpty()) {
            if (db.changes == null || db.changes.data == null || db.changes.data.isEmpty()) {
                db.changes = main.changes;
                return db;
            }
            Map<Integer, Addon> newData = Maps.newHashMap();
            List<Addon> toUse = Lists.newArrayList();
            for (Addon a : db.changes.data) {
                newData.put(a.id, a);
            }
            for (Addon eChange : main.changes.data) {
                if (newData.containsKey(eChange.id)) {
                    toUse.add(newData.get(eChange.id));
                    newData.remove(eChange.id);
                } else {
                    toUse.add(eChange);
                }
            }
            for (Addon a : newData.values()) {
                toUse.add(a);
            }
            db.changes.data = toUse;
        }
        return db;
    }

    public static MergedDatabase mergeDumps (@Nonnull AddonDatabase main, @Nonnull AddonDatabase newDump, boolean forceMerge) {
        if (newDump.timestamp <= main.timestamp || (newDump.timestamp > main.timestamp && forceMerge)) {
            return new MergedDatabase(main);//don't merge into newer dump files by default
        }
        MergedDatabase merged = new MergedDatabase();
        merged.changes = new AddonDatabase();
        merged.changes.data = Lists.newArrayList();
        List<Addon> mainList = Lists.newArrayList(main.data);
        for (Addon newAddon : newDump.data) {
            boolean needsAdding = true;

            for (int n = 0; n < mainList.size(); n++) {
                Addon item = mainList.get(n);
                if (item.id == newAddon.id) {
                    if (!item.equals(newAddon) || forceMerge) {
                        if (debug) {
                            String old = JsonFactory.GSON.toJson(item);
                            String nw = JsonFactory.GSON.toJson(newAddon);
                            log.debug("Old {}\n\n\nNew {}", old, nw);
                        }
                        mainList.set(n, newAddon);
                        merged.changes.data.add(newAddon);
                    }
                    needsAdding = false;
                    break;
                }
            }

            if (needsAdding) {
                mainList.add(newAddon);
                merged.changes.data.add(newAddon);
            }

        }

        main.data = mainList;
        merged.oldTimestamp = main.timestamp;
        merged.changes.timestamp = newDump.timestamp;
        main.timestamp = newDump.timestamp;
        merged.currentDatabase = main;
        return merged;
    }

    public static MergedDatabase updateCompleteDatabaseIfNeeded (@Nonnull AddonDatabase db, @Nonnull String game) {
        Map<DatabaseType, Long> timestamps = getTimestamps(game);
        MergedDatabase merged = null;
        long timestamp = db.timestamp;
        for (DatabaseType dbType : DatabaseType.values()) {
            long ts = timestamps.get(dbType);
            if (ts > timestamp) {
                AddonDatabase newDb = getDatabase(game, dbType);
                if (merged == null) {
                    merged = mergeDumps(db, newDb, false);
                    merged.newDBTypes = Lists.newArrayList();
                    merged.newDBTypes.add(dbType);
                    timestamp = merged.currentDatabase.timestamp;
                } else {
                    List<DatabaseType> types = merged.newDBTypes;
                    merged = mergeDumps(merged, newDb, false);
                    merged.newDBTypes = types;
                    merged.newDBTypes.add(dbType);
                }
            }
        }
        if (merged == null) {
            return new MergedDatabase(db);
        } else {
            return merged;
        }
    }

    public static Map<DatabaseType, Long> getTimestamps (String game) {
        Map<DatabaseType, Long> ret = Maps.newHashMap();
        for (DatabaseType t : DatabaseType.values()) {
            ret.put(t, getTimestamp(game, t));
        }
        return ret;
    }

}
