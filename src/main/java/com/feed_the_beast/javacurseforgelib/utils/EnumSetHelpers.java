package com.feed_the_beast.javacurseforgelib.utils;

import java.util.EnumSet;
import java.util.Set;

public class EnumSetHelpers {
    private EnumSetHelpers() {
    }

    public static <T extends Enum<T> & BetterEnum<Long>> long serialize(Set<T> set, Class<T> classOfT) {
        if (EnumSet.noneOf(classOfT).equals(set)) {
            return 0;
        }
        if (EnumSet.allOf(classOfT).equals(set)) {
            return -1;
        }

        long result = 0;
        for (T gp : set ) {
            result += gp.getValue();
        }
        return result;
    }

    public static <T extends Enum<T> & BetterEnum<Long>> EnumSet<T> deserialize(long l, Class<T> classOfT) {
        // handle special cases
        if (l == 0) {
            return EnumSet.noneOf(classOfT);
        }
        if (l == -1) {
            return EnumSet.allOf(classOfT);
        }

        // deserialize
        EnumSet<T> result = EnumSet.noneOf(classOfT);
        for (T gp : classOfT.getEnumConstants()) {
            if ((l & gp.getValue()) == gp.getValue()) {
                result.add(gp);
                l = l - gp.getValue();
            }
        }

        if (l != 0) {
            throw new IllegalStateException("All enums not found. remainder: " + l + " collected enums: " + result);
        }

        return result;
    }
}
