package com.luxx.common.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    boolean containsKey(K key);

    void put(K key, V value);

    V put(K key, V value, long ttl, TimeUnit unit);

    void expire(K key);

    void clear();

    V get(K key);

    Set<K> keys();

    Collection<V> values();

    Map<K, V> getAll();

    long size();

    void setMaxSize(long maxSize);

    void close();

    double getHitRate();
}
