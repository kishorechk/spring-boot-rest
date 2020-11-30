package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.mapper.ServerMapperConfiguration;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.ChecksumService;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.impl.ServerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ServerServiceTests {

    @Mock
    private ChecksumService checksumServiceMock;

    @Mock
    private DataBodyService dataBodyServiceImplMock;

    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    private Server server;

    @Before
    public void setup() {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));

        server = new ServerImpl(dataBodyServiceImplMock, modelMapper, checksumServiceMock);
    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException {
        when(checksumServiceMock.getChecksum(testDataEnvelope.getDataBody().getDataBody()))
                .thenReturn(testDataEnvelope.getDataHeader().getChecksum());
        boolean success = server.saveDataEnvelope(testDataEnvelope);
        assertThat(success).isTrue();
        verify(dataBodyServiceImplMock, times(1)).saveDataBody(any());
        //verify(hadoopClientMock, times(1)).sendDataToHadoop(any(DataEnvelope.class));
    }
}
