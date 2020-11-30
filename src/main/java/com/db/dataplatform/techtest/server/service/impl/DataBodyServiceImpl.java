package com.db.dataplatform.techtest.server.service.impl;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.repository.DataStoreRepository;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.service.DataHeaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataBodyServiceImpl implements DataBodyService {

    private final DataStoreRepository dataStoreRepository;
    private final DataHeaderService dataHeaderService;

    @Override
    public void saveDataBody(DataBodyEntity dataBody) {
        dataStoreRepository.save(dataBody);
    }

    @Override
    public List<DataBodyEntity> getDataByBlockType(BlockTypeEnum blockType) {
        List<DataHeaderEntity> dataHeaderEntities = dataHeaderService.getHeaderByBlockType(blockType);
    	List<DataBodyEntity> dataBodyEntities = dataStoreRepository.findByDataHeaderEntityIn(dataHeaderEntities);
        return dataBodyEntities;
    }

    @Override
    public Optional<DataBodyEntity> getDataByBlockName(String blockName) {
        return null;
    }
}
