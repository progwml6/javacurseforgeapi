package com.feed_the_beast.javacurseforgelib.addondumps;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Category {
    public int id;
    public String name;
    @SerializedName("URL")
    public String url;
}
