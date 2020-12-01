package com.db.dataplatform.techtest.server.api.controller;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.service.ChecksumService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws IOException, NoSuchAlgorithmException {
        log.info("Data envelope received: {} {}", dataEnvelope.getDataHeader().getName(), dataEnvelope.getDataBody().getDataBody());

            boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

            log.info("Data envelope persisted. Attribute name: {}", dataEnvelope.getDataHeader().getName());

            return ResponseEntity.ok(checksumPass);
    }

    @GetMapping(value = "/data/{blockType}")
    public ResponseEntity<List<DataEnvelope>> getDataBodyByBlockType(@NotNull @PathVariable("blockType") BlockTypeEnum blockType){
    	log.info("Request for data blocks by block type");
    	List<DataEnvelope> body = server.getDataBodyByBlockType(blockType);
    	return ResponseEntity.ok(body);
    }

    @PatchMapping(value = "/update/{name}/{newBlockType}")
    public ResponseEntity<Boolean> patchBlockTypeByBlockName(@NotNull @PathVariable("name") String name, @NotNull @PathVariable("newBlockType") String newBlockType) {
        log.info("Request for update block type by block name {} {}", name, newBlockType);
        boolean result = server.updateBlockTypeByBlockName(name, BlockTypeEnum.valueOf(newBlockType));
        return ResponseEntity.ok(result);
    }

}
