package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.service.HadoopClientService;
import com.db.dataplatform.techtest.server.service.impl.HadoopClientServiceImpl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class HadoopClientServiceTests {
    @Mock
    private RestTemplate restTemplate;
    private HadoopClientService hadoopClientService;

    private static final String HADOOP_URL = "hadoop.datalake.url";

    @Before
    public void setup() {
        hadoopClientService = new HadoopClientServiceImpl(restTemplate) ;
        ReflectionTestUtils.setField(hadoopClientService, "HADOOP_URL", HADOOP_URL);
    }

    @Test
    public void testHadoopClientCalled() {
        DataEnvelope dataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();
        hadoopClientService.sendDataToHadoop(dataEnvelope);
        verify(restTemplate,times(1)).postForEntity(HADOOP_URL, dataEnvelope, String.class);
    }
}
