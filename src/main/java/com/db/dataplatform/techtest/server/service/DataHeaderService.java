package com.db.dataplatform.techtest.server.service;

import java.util.List;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;

public interface DataHeaderService {
    void saveHeader(DataHeaderEntity entity);
    List<DataHeaderEntity> getHeaderByBlockType(BlockTypeEnum blockType);
    DataHeaderEntity getHeaderByBlockName(String blockName);
}
