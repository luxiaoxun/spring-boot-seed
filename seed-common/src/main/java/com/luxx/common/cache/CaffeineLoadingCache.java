package com.luxx.common.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CaffeineLoadingCache<K, V> implements Cache<K, V> {

    private LoadingCache<K, V> cache = null;

    public CaffeineLoadingCache(long maxSize, CacheLoader<? super K, V> loader) {
        cache = Caffeine.newBuilder().maximumSize(maxSize).recordStats().build(loader);
    }

    public CaffeineLoadingCache(long maxSize, Duration expireDuration, CacheLoader<? super K, V> loader) {
        cache = Caffeine.newBuilder().maximumSize(maxSize).expireAfterWrite(expireDuration).recordStats().build(loader);
    }

    public CaffeineLoadingCache(long maxSize, Duration expireDuration, Duration refreshDuration, CacheLoader<? super K, V> loader) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder().maximumSize(maxSize);
        if (expireDuration != null) {
            caffeine = caffeine.expireAfterWrite(expireDuration);
        }
        if (refreshDuration != null) {
            caffeine = caffeine.refreshAfterWrite(refreshDuration);
        }
        cache = caffeine.recordStats().build(loader);
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
        return cache.get(key);
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
    public Map<K, V> getAll() {
        return cache.asMap();
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
