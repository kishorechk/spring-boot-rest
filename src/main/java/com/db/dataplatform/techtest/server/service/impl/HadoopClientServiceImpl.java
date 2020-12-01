package com.db.dataplatform.techtest.server.service.impl;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.service.HadoopClientService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.NOT_SUPPORTED)
public class HadoopClientServiceImpl implements HadoopClientService {

    private final RestTemplate restTemplate;

    @Value("${hadoop.datalake.url}")
    private String HADOOP_URL;

    @Override
    public void sendDataToHadoop(DataEnvelope envelope) {
        try {
            log.info("Sending data to hadoop");
            restTemplate.postForEntity(HADOOP_URL, envelope, String.class);
            log.info("Successfully sent data to hadoop");
        } catch(Exception ex) {
            log.info("Failed to send data to hadoop {}", ex);
        }
    }
}
