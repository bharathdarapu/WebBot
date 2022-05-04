// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.ms.cloudathon2022;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This is the default controller that will receive incoming Channel Activity
 * messages.
 *
 * <p>
 * This controller is suitable in most cases. Bots that want to use this
 * controller should do so by using the @Import({BotController.class})
 * annotation. See any of the samples Application class for an example.
 * </p>
 */
@RestController
public class MainController {
    /**
     * The slf4j Logger to use. Note that slf4j is configured by providing Log4j
     * dependencies in the POM, and corresponding Log4j configuration in the
     * 'resources' folder.
     */
    private Logger logger = LoggerFactory.getLogger(MainController.class);
    
    
    @GetMapping("/test")
    public String sayHi()
    {
    	return "Hello World!";
    }

    @GetMapping("/api/getIndividualPerformance")
    public @ResponseBody ResponseEntity<String> getCustomModelPerformance(@RequestParam String individualPerformanceFileToken) {
    	
    	/*String fileContent ="";
    	try {
    	
    	String tempDir=System.getProperty("java.io.tmpdir")+fileName;
    	fileContent = new String(Files.readAllBytes(Paths.get(tempDir)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		} */
    	
    	
    	
        return new ResponseEntity<String>(readFromAzureBlob(individualPerformanceFileToken), HttpStatus.OK);
    }
    
    @GetMapping("/api/getCombinedPerformance")
    public @ResponseBody ResponseEntity<String> getCustomModelPerformance(@RequestParam String customModelPerformanceFileToken,@RequestParam String indexFundPerformanceFileToken, @RequestParam String simulatedIndexPerformanceFileToken) throws IOException {
    	
    	
    	String customModelPerformance = "";
    	String indexFundPerformance = "";
    	String simulatedIndexPerformance = "";
    	
    	JsonObject consolidatedJson = new JsonObject();
    	
    	customModelPerformance = readFromAzureBlob(customModelPerformanceFileToken);
    	indexFundPerformance = readFromAzureBlob(indexFundPerformanceFileToken);
    	simulatedIndexPerformance = readFromAzureBlob(simulatedIndexPerformanceFileToken);
    	
    	JsonParser jsonParser = new JsonParser();
    	JsonArray customModelPerformanceJson = (JsonArray) jsonParser.parse(customModelPerformance);
    	JsonArray indexFundPerformanceJson = (JsonArray) jsonParser.parse(indexFundPerformance);
    	JsonArray simulatedIndexPerformanceJson = (JsonArray) jsonParser.parse(simulatedIndexPerformance);
    	
    	consolidatedJson.add("customModelPerformace", customModelPerformanceJson);
    	consolidatedJson.add("indexFundPerformance", indexFundPerformanceJson);
    	consolidatedJson.add("simulatedIndexPerformance", simulatedIndexPerformanceJson); 
    	
        return new ResponseEntity<String>(consolidatedJson.toString(), HttpStatus.OK);
    }
    
    
    public Path createTempFile()
    {
    	Path endFilePath = null;
    	try {
    	Path filePath=Files.createTempFile("DIS_BOT_"+java.util.UUID.randomUUID(), ".json");
    	System.out.println("Temp file Path: " + filePath);
    	System.out.println("Temp file : " + filePath.getFileName());
    	endFilePath = filePath;
    	Files.delete(endFilePath);
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return endFilePath;
    }
    
    public String readFromFile(Path path)
    {
    	byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return new String(encoded,StandardCharsets.UTF_8);
    }
    
    public String readFromAzureBlob(String fileToken)
    {
    	
    	String[] decodedString = new String(Base64.getDecoder().decode(fileToken)).split(":");
    	
    	String containerName = decodedString[0];
    	String fileName = decodedString[1];
    	Path tempPath = null;
    	
    	System.out.println("***********************************");
    	System.out.println("decodedString: "+decodedString);
    	System.out.println("***********************************");
    	
    	try {
    		
    		tempPath = createTempFile();
    		//Not a great idea
    		String connectStr = "DefaultEndpointsProtocol=https;AccountName=disbotblobstorage;AccountKey=237Mp0RXDV430m7qlzOYuIk9iV8LXSMMBC/n4XAIKp6/HgW0rvAF0dfMH3dYIhDCT6ablpmIFcVF+AStROUMKA==;EndpointSuffix=core.windows.net";
    		
    		System.out.println("ConnectionString:"+connectStr);
    		
    		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();
    		BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
    		
    	BlobClient blobClient = containerClient.getBlobClient(fileName);
    	blobClient.downloadToFile(tempPath.toString());
    	
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return readFromFile(tempPath);
    }
    
}
