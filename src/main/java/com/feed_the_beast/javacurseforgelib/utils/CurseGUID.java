package com.feed_the_beast.javacurseforgelib.utils;

import javax.annotation.Nonnull;
import java.util.UUID;

public class CurseGUID {
    public enum Type {
        UUID(0), INT(1), TWO_INTS(1), UUID_TWO_INTS(2);

        private long value;

        Type(int value) {
            this.value = value;
        }
    }

    private final UUID uuid;
    private final long intLeft;
    private final long intRight;
    private final Type type;

    public Type getType() {
        return type;
    }

    private CurseGUID(@Nonnull UUID uuid) {
        this.type = Type.UUID;
        this.uuid = uuid;
        this.intLeft = this.intRight = 0;
    }

    private CurseGUID(long intLeft) {
        this.type = Type.INT;
        this.intLeft = intLeft;
        this.uuid = null;
        this.intRight = 0;
    }

    private CurseGUID(long intLeft, long intRight) {
        this.type = Type.TWO_INTS;
        this.intLeft = intLeft;
        this.intRight = intRight;
        this.uuid = null;
    }

    private CurseGUID(@Nonnull UUID uuid, long intLeft, long intRight) {
        this.type = Type.UUID_TWO_INTS;
        this.uuid = uuid;
        this.intLeft = intLeft;
        this.intRight = intRight;
    }

    public static  CurseGUID newInstance(String s) {
        return deserialize(s);
    }

    public static CurseGUID newInstance(UUID u) {
        return new CurseGUID(u);
    }

    public static CurseGUID newInstance(long l) {
        return new CurseGUID(l);
    }

    public static CurseGUID newInstance(long l, long r) {
        return new CurseGUID(l, r);
    }

    public static CurseGUID newInstance(UUID uuid, long l, long r) {
        return new CurseGUID(uuid, l, r);
    }

    public static CurseGUID newRandomUUID() {
        return new CurseGUID(UUID.randomUUID());
    }

    public static CurseGUID newInstance() {
        return new CurseGUID(new UUID(0,0));
    }

    public String serialize() {
        switch (getType()) {
            case UUID:
                //noinspection ConstantConditions
                return uuid.toString();
            case INT:
                return Long.toString(intLeft);
            case TWO_INTS:
                return Long.toString(intLeft) + ":" + Long.toString(intRight);
            case UUID_TWO_INTS:
                //noinspection ConstantConditions
                return uuid.toString() + ":" + Long.toString(intLeft) + ":" + Long.toString(intRight);
        }
        throw new IllegalStateException();
    }

    public static CurseGUID deserialize(String s) {
        CurseGUID result;
        if (s.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")) {
            result = CurseGUID.newInstance(UUID.fromString(s));
        } else if (s.matches("[0-9]*")) {
            long l = Long.parseLong(s);
            result = CurseGUID.newInstance(l);
        } else if (s.matches("[0-9]*:[0-9]*")) {
            String[] splitted = s.split(":");
            long l = Long.parseLong(splitted[0]);
            long r = Long.parseLong(splitted[1]);
            result = CurseGUID.newInstance(l, r);
        } else if(s.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}:[0-9]*:[0-9]*")) {
            String[] splitted = s.split(":");
            UUID uuid = UUID.fromString(splitted[0]);
            long l = Long.parseLong(splitted[1]);
            long r = Long.parseLong(splitted[2]);
            result = CurseGUID.newInstance(uuid, l, r);
        } else {
            throw new IllegalStateException("No matched desrializer for " + s);
        }
        return result;
    }

    @Override
    public String toString() {
        return serialize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurseGUID curseGUID = (CurseGUID) o;

        if (type != curseGUID.type) return false;
        switch (type) {
            case UUID:
                //noinspection ConstantConditions
                return uuid.equals(curseGUID.uuid);
            case INT:
                return intLeft == curseGUID.intLeft;
            case TWO_INTS:
                return intLeft == curseGUID.intLeft
                        && intRight == curseGUID.intRight;
            case UUID_TWO_INTS:
                //noinspection ConstantConditions
                return intLeft == curseGUID.intLeft
                        && intRight == curseGUID.intRight
                        && uuid.equals(curseGUID.uuid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = uuid == null ? 0 :uuid.hashCode();
        result = 31 * result + (int) (intLeft ^ (intLeft >>> 32));
        result = 31 * result + (int) (intRight ^ (intRight >>> 32));
        result = 31 * result + type.hashCode();
        return result;
    }

    public static boolean isCurseGUID(String s) {
        return s.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")
                || s.matches("[0-9]*")
                || s.matches("[0-9]*:[0-9]*")
                || s.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}:[0-9]*:[0-9]*");
    }
}
