package com.feed_the_beast.javacurseforgelib.addondumps;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nonnull;

public class Filtering {

    //TODO setup caches of filtered data by categorySection, game version, categories

    public static Optional<Addon> getAddonById (int id, @Nonnull AddonDatabase db) {
        if (id == -1) {
            return Optional.empty();
        }

        for (Addon a : db.data) {
            if (a.id == id) {
                return Optional.of(a);
            }
        }

        return Optional.empty();
    }

    /**
     *
     * @param projectID intgeger project id #
     * @param db Addon database
     * @return the slug used in most curseforge urls
     */
    public static Optional<String> getAddonSlug (int projectID, @Nonnull AddonDatabase db) {
        Optional<Addon> a = getAddonById(projectID, db);
        if (a.isPresent()) {
            return Optional.of(a.get().getSlug());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Filters list of addons for a single author
     * @param author author to filter by
     * @param db AddonDatabase to filter
     * @return a list of filtered addons, or an empty list if none found
     */
    public static List<Addon> byAuthor (@Nonnull String author, @Nonnull AddonDatabase db) {
        List<Addon> ret = Lists.newArrayList();
        author = author.toLowerCase(Locale.ENGLISH);
        for (Addon a : db.data) {
            filterAuthor(ret, a, author);
        }
        return ret;
    }

    /**
     *
     * @param author author to filter by
     * @param section section to filter by(Mods, Texture Packs, Modpacks, etc.)
     * @param db AddonDatabase to filter
     * @return a list of filtered addons, or an empty list if none found
     */
    public static List<Addon> byAuthorAndCategorySection (@Nonnull String author, @Nonnull String section, @Nonnull AddonDatabase db) {
        List<Addon> ret = Lists.newArrayList();
        author = author.toLowerCase(Locale.ENGLISH);
        section = section.toLowerCase(Locale.ENGLISH);
        for (Addon a : db.data) {
            if (a.categorySection.name.toLowerCase(Locale.ENGLISH).equals(section)) {
                ret = filterAuthor(ret, a, author);
            }
        }
        return ret;

    }

    /**
     *
     * @param section section to filter by(Mods, Texture Packs, Modpacks, etc.)
     * @param db AddonDatabase to filter
     * @return a list of filtered addons, or an empty list if none found
     */
    public static List<Addon> byCategorySection (@Nonnull String section, @Nonnull AddonDatabase db) {
        List<Addon> ret = Lists.newArrayList();
        section = section.toLowerCase(Locale.ENGLISH);
        for (Addon a : db.data) {
            if (a.categorySection.name.toLowerCase(Locale.ENGLISH).equals(section)) {
                ret.add(a);
            }
        }
        return ret;

    }

    private static List<Addon> filterAuthor (@Nonnull List<Addon> ret, @Nonnull Addon a, @Nonnull String author) {
        if (a.primaryAuthorName.toLowerCase(Locale.ENGLISH).equals(author)) {
            ret.add(a);
        } else {
            for (Author auth : a.authors) {
                if (auth.name.toLowerCase(Locale.ENGLISH).equals(author)) {
                    ret.add(a);
                    break;
                }
            }
        }
        return ret;
    }
}
