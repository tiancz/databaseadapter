package com.tian.adapter.meta.scheme;

public enum IndexType {
    statistic(0),
    clustered(1),
    hashed(2),
    other(3);

    private final int id;

    IndexType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
