package com.feed_the_beast.javacurseforgelib.addondumps;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
//@EqualsAndHashCode(exclude = {"downloadCount", "installCount" , "popularityScore", "rating", "likes"})
/**
 * curse addons class
 * downloadCount, installCount and popularityScore, rating, likes are not checked in equals/hashcode currently
 * a secondary equals method needs to be made that ignores these at some point for not triggering update events
 * when analytics change
 */
public class Addon {
    public int id;
    public String name;
    public List<Author> authors;
    public List<Attachment> attachments;
    @SerializedName("WebSiteURL")
    public String websiteURL;
    public int gameId;
    public String summary;
    public int defaultFileId;
    public int commentCount;
    public double downloadCount;
    public int rating;
    public int installCount;
    public int iconId;
    public List<LatestFile> latestFiles;
    public List<Category> categories;
    public String primaryAuthorName;
    public String externalUrl;
    public int status;
    public int stage;
    public String donationUrl;
    public int primaryCategoryId;
    public String primaryCategoryName;
    public String primaryCategoryAvatarUrl;
    public int likes;
    public CategorySection categorySection;
    public int packageType;
    public String avatarUrl;
    public List<GameVersionLatestFile> gameVersionLatestFiles;
    public int isFeatured;
    public double popularityScore;

    //cleans out URL part -- we want the last chunk minus any trailing slashes
    public String getSlug () {
        String ret = "";
        while (websiteURL.charAt(websiteURL.length() - 1) == '/') {
            ret = websiteURL.substring(0, websiteURL.length() - 1);
        }
        int pos = ret.lastIndexOf('/');
        if (pos >= 0) {
            ret = ret.substring(pos + 1, ret.length());
        }
        return ret;
    }
}
