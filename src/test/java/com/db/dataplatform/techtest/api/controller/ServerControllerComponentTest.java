package com.db.dataplatform.techtest.api.controller;

import static com.db.dataplatform.techtest.TestDataHelper.DUMMY_DATA;
import static com.db.dataplatform.techtest.TestDataHelper.MD5_CHECKSUM;
import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import com.db.dataplatform.techtest.server.api.controller.ServerController;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.exception.HadoopClientException;
import com.db.dataplatform.techtest.server.component.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ServerControllerComponentTest {

	public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
	public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
	public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

	@Mock
	private Server serverMock;

	private DataEnvelope testDataEnvelope;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	private ServerController serverController;

	@Before
	public void setUp() throws HadoopClientException, NoSuchAlgorithmException, IOException {
		serverController = new ServerController(serverMock);
		mockMvc = standaloneSetup(serverController).build();
		objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.build();

			testDataEnvelope = createTestDataEnvelopeApiObject();

		when(serverMock.saveDataEnvelope(any(DataEnvelope.class))).thenReturn(true);
	}

	@Test
	public void testPushDataPostCallWorksAsExpected() throws Exception {

		String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

		MvcResult mvcResult = mockMvc.perform(post(URI_PUSHDATA)
				.content(testDataEnvelopeJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();
	}

	@Test
	public void testGetDataForBlockTypeWorksAsExpected() throws Exception {
		List<DataEnvelope> returnObj = new ArrayList<>();
		returnObj.add(createTestDataEnvelopeApiObject());
		when(serverMock.getDataBodyByBlockType(any())).thenReturn(returnObj);

		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("blockType", "BLOCKTYPEA");
		mockMvc.perform(get(URI_GETDATA.expand(uriVariables)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].dataHeader.name", is(TEST_NAME)))
				.andExpect(jsonPath("$[0].dataHeader.blockType", is("BLOCKTYPEA")))
				.andExpect(jsonPath("$[0].dataHeader.bodyMD5Signature", is(MD5_CHECKSUM)))
				.andExpect(jsonPath("$[0].dataBody.dataBody", is(DUMMY_DATA)));
		verify(serverMock, times(1)).getDataBodyByBlockType(any());
	}
}
