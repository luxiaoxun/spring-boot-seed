package com.luxx.seed.service;

import com.luxx.seed.jpa.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public class JpaService<V extends BaseEntity> {
    private static final Sort SORT = Sort.by(Sort.Direction.DESC, "id");

    @Autowired
    private JpaRepository<V, Long> jpaRepository;

    protected <T extends JpaRepository<V, Long>> T getJpaRepository() {
        return (T) jpaRepository;
    }

    public V save(V v) {
        return jpaRepository.save(v);
    }

    public List<V> saveAll(Collection<V> v) {
        return jpaRepository.saveAll(v);
    }

    public List<V> findAll() {
        return jpaRepository.findAll();
    }

    public List<V> findAllById(Collection<Long> ids) {
        return jpaRepository.findAllById(ids);
    }

    public V findById(long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    public Page<V> findAll(V entity, int pageNum, int pageSize) {
        return findAll(Example.of(entity), pageSize, pageNum);
    }

    public Page<V> findAll(Example<V> example, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, SORT);
        return getJpaRepository().findAll(example, pageable);
    }

}
