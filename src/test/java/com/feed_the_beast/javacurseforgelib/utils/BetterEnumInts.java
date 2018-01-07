package com.feed_the_beast.javacurseforgelib.utils;

import javax.annotation.Nonnull;

public enum BetterEnumInts implements BetterEnum<Integer> {
    NULL(0), ADAMS(42), BEAST(666), MIN(Integer.MIN_VALUE), MAX(Integer.MAX_VALUE);

    private int value;

    BetterEnumInts(int value) {
        this.value = value;
    }

    @Nonnull
    @Override
    public Integer getValue() {
        return value;
    }
}
