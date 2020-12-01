package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.service.ChecksumService;
import com.db.dataplatform.techtest.server.service.impl.MD5ChecksumService;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class CheckSumServiceTests {
    ChecksumService checksumService;

    @Before
    public void setup() {
        checksumService = new MD5ChecksumService();
    }

    @Test
    public void testMD5Signaturehashing() throws NoSuchAlgorithmException {
        String result = checksumService.getChecksum(TestDataHelper.DUMMY_DATA);
        assertThat(result).isEqualTo(TestDataHelper.MD5_CHECKSUM);
    }
}
