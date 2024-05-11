package com.luxx.cache;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CaffeineLocalCache<K, V> implements Cache<K, V> {

    private com.github.benmanes.caffeine.cache.Cache<K, V> cache = null;

    public CaffeineLocalCache(long maxSize, Duration expireDuration, boolean recordStats) {
        if (recordStats) {
            cache = Caffeine.newBuilder().maximumSize(maxSize).expireAfterWrite(expireDuration).recordStats().build();
        } else {
            cache = Caffeine.newBuilder().maximumSize(maxSize).expireAfterWrite(expireDuration).build();
        }
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V put(K key, V value, long ttl, TimeUnit unit) {
        throw new RuntimeException("RuntimeException");
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Map<K, V> getAll() {
        return cache.asMap();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.asMap().containsKey(key);
    }

    @Override
    public void expire(K key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }

    @Override
    public Set<K> keys() {
        return cache.asMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return cache.asMap().values();
    }

    @Override
    public long size() {
        return cache.asMap().size();
    }

    @Override
    public void setMaxSize(long maxSize) {
        cache.policy().eviction().ifPresent(eviction -> {
            eviction.setMaximum(maxSize);
        });
    }

    @Override
    public void close() {
        cache.cleanUp();
    }

    @Override
    public double getHitRate() {
        return cache.stats().hitRate();
    }
}
