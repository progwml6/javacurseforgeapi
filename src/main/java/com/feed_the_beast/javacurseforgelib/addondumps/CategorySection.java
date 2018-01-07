package com.feed_the_beast.javacurseforgelib.addondumps;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CategorySection {
    @SerializedName("ID")
    public int id;
    public int gameID;
    public String name;
    public int packageType;
    public String path;
    public String initialInclusionPattern;
    public String extraIncludePattern;
}
