package com.feed_the_beast.javacurseforgelib.utils;

import javax.annotation.Nonnull;

public enum BetterEnumLongs implements BetterEnum<Long> {
    NULL(0), ADAMS(42), BEAST(666), MIN(Long.MIN_VALUE), MAX(Long.MAX_VALUE);

    private long value;

    BetterEnumLongs(long value) {
        this.value = value;
    }

    @Nonnull
    @Override
    public Long getValue() {
        return value;
    }
}