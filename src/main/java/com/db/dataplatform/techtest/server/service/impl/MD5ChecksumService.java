package com.db.dataplatform.techtest.server.service.impl;

import com.db.dataplatform.techtest.server.service.ChecksumService;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
class MD5ChecksumService implements ChecksumService {
   public String getChecksum(String input) throws NoSuchAlgorithmException {
       byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes());
       String checksum = new BigInteger(1, digest).toString(16);
       return checksum;
   }
}