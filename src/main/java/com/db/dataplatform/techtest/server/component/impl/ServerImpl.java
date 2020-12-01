package com.db.dataplatform.techtest.server.component.impl;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.ChecksumService;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.service.HadoopClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;
    private final ModelMapper modelMapper;

    private final ChecksumService checksumService;

    private final HadoopClientService hadoopService;
    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) throws NoSuchAlgorithmException {

        String checksum = checksumService.getChecksum(envelope.getDataBody().getDataBody());
        boolean checksumPass = checksum.equals(envelope.getDataHeader().getChecksum());
        if(checksumPass) {
            // Save to persistence.
            persist(envelope);
            //push to hadoop
            hadoopService.sendDataToHadoop(envelope);
            log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
        } else {
            log.info("Data not persisted due to invalid checksum, data name: {}"
                    , envelope.getDataHeader().getName());
        }

        return checksumPass;
    }

    /**
     * @param blockType
     * @return all persisted blocks that have a given block type
    */
    @Override
	public List<DataEnvelope> getDataBodyByBlockType(BlockTypeEnum blockType) {
        log.info("get list of data blocks by block type: {}", blockType.toString());
		List<DataBodyEntity> dataBodyEntities =  dataBodyServiceImpl.getDataByBlockType(blockType);
		return dataBodyEntities.stream().map(dataBodyEntity-> new DataEnvelope(
				modelMapper.map(dataBodyEntity.getDataHeaderEntity(), DataHeader.class),
				modelMapper.map(dataBodyEntity, DataBody.class))).collect(Collectors.toList());
	}

    @Override
    public boolean updateBlockTypeByBlockName(String blockName, BlockTypeEnum blockType) {
        Optional<DataBodyEntity> dataBodyEntityBox = dataBodyServiceImpl.getDataByBlockName(blockName);
        if(!dataBodyEntityBox.isPresent()) return false;
        DataBodyEntity dataBodyEntity = dataBodyEntityBox.get();
        dataBodyEntity.getDataHeaderEntity().setBlocktype(blockType);
        saveData(dataBodyEntity);
        return true;
    }

    private void persist(DataEnvelope envelope) {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);

        saveData(dataBodyEntity);
    }

    private void saveData(DataBodyEntity dataBodyEntity) {
        dataBodyServiceImpl.saveDataBody(dataBodyEntity);
    }

}
