package com.feed_the_beast.javacurseforgelib.addondumps;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class AddonDatabase {
    @SerializedName("data")
    public List<Addon> data;
    @SerializedName("timestamp")
    public long timestamp;
}
