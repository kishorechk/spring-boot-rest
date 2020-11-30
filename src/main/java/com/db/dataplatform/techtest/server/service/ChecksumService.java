package com.db.dataplatform.techtest.server.service;

import java.security.NoSuchAlgorithmException;

public interface ChecksumService {
    String getChecksum(String input) throws NoSuchAlgorithmException;
}