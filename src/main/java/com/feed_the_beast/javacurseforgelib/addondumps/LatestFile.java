package com.feed_the_beast.javacurseforgelib.addondumps;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LatestFile {
    public int id;
    public String fileName;
    public String fileNameOnDisk;
    public Date fileDate;
    public ReleaseType releaseType;
    public int fileStatus;
    public String downloadURL;
    public boolean isAlternate;
    public int alternateFileId;
    public List<Dependency> Dependencies;
    public boolean isAvailable;
    public List<Module> modules;
    public long packageFingerprint;
    public List<String> gameVersion;

}
