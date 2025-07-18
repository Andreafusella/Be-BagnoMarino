package com.bagno.marino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<M, T> extends JpaRepository<M, T>, JpaSpecificationExecutor<M> {
}
