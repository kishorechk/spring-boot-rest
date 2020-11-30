package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataStoreRepository extends JpaRepository<DataBodyEntity, Long> {
    List<DataBodyEntity> findByDataHeaderEntityIn(List<DataHeaderEntity> dataHeaderEntities);
}
