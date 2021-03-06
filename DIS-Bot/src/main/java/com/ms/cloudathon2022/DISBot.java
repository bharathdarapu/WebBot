// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.ms.cloudathon2022;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ActivityTypes;
import com.microsoft.bot.schema.AnimationCard;
import com.microsoft.bot.schema.Attachment;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.CardImage;
import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.HeroCard;
import com.microsoft.bot.schema.MediaUrl;
import com.microsoft.bot.schema.SuggestedActions;
import com.microsoft.bot.schema.TextFormatTypes;
import com.ms.cloudathon2022.dissimulator.Simulator;
import com.ms.cloudathon2022.model.Model;
import com.ms.cloudathon2022.model.ModelPerformance;
import com.ms.cloudathon2022.model.ModelPerformanceCalculator;
import com.ms.cloudathon2022.security.Security;
import com.ms.cloudathon2022.security.SecurityMap;


/**
 * This class implements the functionality of the Bot.
 *
 * <p>
 * This is where application specific logic for interacting with the users would be added. For this
 * sample, the {@link #onMessageActivity(TurnContext)} echos the text back to the user. The {@link
 * #onMembersAdded(List, TurnContext)} will send a greeting to new conversation participants.
 * </p>
 */
public class DISBot extends ActivityHandler {

	private static Model model = new Model();
	private static Model indexFund = new Model();
	
	private String customModelFileToken;
	private String customModelPerformanceFileToken = "";
	private String indexFundPerformanceFileToken= "";
	private String simulatedIndexFundPerformanceFileToken = "";
	
	
	
    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        
    	String inputText = turnContext.getActivity().getText();
    	String responseText;
    	
    	List<ModelPerformance> customModelPerformace = new ArrayList<ModelPerformance>();
    	List<ModelPerformance> indexFundPerformace = new ArrayList<ModelPerformance>();
    	List<ModelPerformance> simulatedModelPerformance = new ArrayList<ModelPerformance>();
    	
    	try {
    		if((inputText.toLowerCase().contains("hey")) || (inputText.toLowerCase().contains("hello"))) {
    			return turnContext.sendActivity(welcomeSuggestedActions()).thenApply(sendResult -> null);
        	} else if(inputText.equalsIgnoreCase("What can you do?")) {
    		responseText = " - You can ask me to create a model and specifiy the allocations.\n\n "
    				+ "- You can ask me to get the performance of an index fund. \n\n "
    				+ "- You can ask me to simulate a direct index (will consider the technology sector based stocks in DOW).";
    	
    	} else if(inputText.toLowerCase().contains("shrug")) {
    		return turnContext.sendActivity(sendShrugGIF()).thenApply(sendResult -> null);
    		
    	}else if((inputText.toLowerCase().contains("create")) && (inputText.toLowerCase().contains("model"))) {
    		model = createModel(inputText);
    		
    		System.out.println("****************************************");
    		System.out.println(model);
    		System.out.println("****************************************");
    		
    		if(Objects.nonNull(model) && model.getAllocation().size() > 0) {
    			//customModelFileName = writeToTempFile("customModel",model.toString());
    			customModelFileToken = writeToAzureBlob("customModel",model.toString());
    			
    			ModelPerformanceCalculator modelPerformanceCalculator = new ModelPerformanceCalculator();
        		customModelPerformace = modelPerformanceCalculator.calculate(model);
        		System.out.println("****************************************");
        		System.out.println("Custom Model Performance");
        		System.out.println(customModelPerformace);
        		System.out.println("****************************************");
        		//customModelPerformanceFileName = writeToTempFile("customModelPerformace",customModelPerformace.toString());
        		customModelPerformanceFileToken = writeToAzureBlob("customModelPerformace",customModelPerformace.toString());
        		responseText = "Model Creation was successful. The remaining balance (if any) was put under cash.\n Model details are as follows:";
        		
        		double totalAllocation =0;
    			StringBuilder allocationBuilder = new StringBuilder();
    			allocationBuilder.append("|Name|Stock|Allocation(%)|\n");
        		allocationBuilder.append("|:-----|:-----|:-----|\n");

        		for(Security security: model.getAllocation())
        		{
        			totalAllocation+=security.getAllocationPercentage();
        			allocationBuilder.append("|"+security.getSecurityName()+"|"+security.getTicker()+"|"+security.getAllocationPercentage()+"|\n");
        		}
        		//add balance cash
        		if(totalAllocation<100)
        		{
        		allocationBuilder.append("|Cash|-|"+(100-totalAllocation)+"|\n");
        		}
    			
    			return turnContext.sendActivities(renderTableandMessage(responseText,allocationBuilder.toString())).thenApply(sendResult -> null);
        		
    		}
    		else
    			responseText = "Model creation failed. Please check the allocation and try creating the model again.";	
    	} else if(((inputText.toLowerCase().contains("print"))||(inputText.toLowerCase().contains("show"))) && (inputText.toLowerCase().contains("model"))) {
    		if(model.getAllocation().size() > 0) {
    			double totalAllocation =0;
    			StringBuilder allocationBuilder = new StringBuilder();
    			allocationBuilder.append("|Name|Stock|Allocation(%)|\n");
        		allocationBuilder.append("|:-----|:-----|:-----|\n");

        		for(Security security: model.getAllocation())
        		{
        			totalAllocation+=security.getAllocationPercentage();
        			allocationBuilder.append("|"+security.getSecurityName()+"|"+security.getTicker()+"|"+security.getAllocationPercentage()+"|\n");
        		}
        		//add balance cash
        		if(totalAllocation<100)
        		{
        		allocationBuilder.append("|Cash|-|"+(100-totalAllocation)+"|\n");
        		}
    			
    			responseText = allocationBuilder.toString();
    			return turnContext.sendActivity(renderTable(responseText)).thenApply(sendResult -> null);
    			
    		}
    		else 
    			responseText = "Model creation failed, please try creating the model again.";	
    	}else if((inputText.toLowerCase().contains("model")) && (inputText.toLowerCase().contains("performance"))) {
    		
    		if(model!=null && model.getAllocation()!=null && (model.getAllocation().size() > 0)) {
    			
    		ModelPerformanceCalculator modelPerformanceCalculator = new ModelPerformanceCalculator();
    		customModelPerformace = modelPerformanceCalculator.calculate(model);
    		System.out.println("****************************************");
    		System.out.println("Custom Model Performance");
    		System.out.println(customModelPerformace);
    		System.out.println("****************************************");
    		//customModelPerformanceFileName = writeToTempFile("customModelPerformace",customModelPerformace.toString());
    		customModelPerformanceFileToken = writeToAzureBlob("customModelPerformace",customModelPerformace.toString());
    		responseText = "Custom model performance simulation complete.";
    		return turnContext.sendActivity(cardIndividualPerformanceMetrics(customModelPerformanceFileToken)).thenApply(sendResult -> null);
    		
    		}
    		else
    			responseText = "Model creation failed, please try creating the model again.";	
    		
    	}else if((inputText.toLowerCase().contains("index")) && (inputText.toLowerCase().contains("fund")) ) {
    		indexFund = createIndexFund(inputText);
    		ModelPerformanceCalculator modelPerformanceCalculator = new ModelPerformanceCalculator();
    		indexFundPerformace = modelPerformanceCalculator.calculate(indexFund);
    		
    		System.out.println("****************************************");
    		System.out.println("Index Fund Performance");
    		System.out.println(indexFundPerformace);
    		System.out.println("****************************************");
    		//indexFundPerformanceFileName = writeToTempFile("indexFundPerformace",indexFundPerformace.toString());
    		indexFundPerformanceFileToken = writeToAzureBlob("indexFundPerformace",indexFundPerformace.toString());
    		responseText = "Index Fund performance simulation complete.";
    		return turnContext.sendActivity(cardIndividualPerformanceMetrics(indexFundPerformanceFileToken)).thenApply(sendResult -> null);

    		
    	}else if((inputText.toLowerCase().contains("simulate"))) {
    	
    		Simulator simulator = new Simulator();
    		simulatedModelPerformance=simulator.simulate();
    		
    		System.out.println("****************************************");
    		System.out.println("Index Fund Simulated Model Performance");
    		System.out.println(simulatedModelPerformance);
    		System.out.println("****************************************");
    		//simulatedIndexFundPerformanceFileName = writeToTempFile("simulatedModelPerformance",simulatedModelPerformance.toString());
    		simulatedIndexFundPerformanceFileToken = writeToAzureBlob("simulatedModelPerformance",simulatedModelPerformance.toString());
    		responseText = "Index Fund based simulated model performance complete.";
    		return turnContext.sendActivity(cardCombinedPerformanceMetrics(customModelPerformanceFileToken,indexFundPerformanceFileToken,simulatedIndexFundPerformanceFileToken)).thenApply(sendResult -> null);
    		
    	}else
    		responseText = "I'm not sure I understand. Can you please repeat?";
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    		responseText = "Hmm. Something went wrong. My monkeys are checking ????. ";
    	}
    	
    	
    		return turnContext.sendActivity(MessageFactory.text(responseText) ).thenApply(sendResult -> null);
    }
    

    @Override
    protected CompletableFuture<Void> onMembersAdded(
        List<ChannelAccount> membersAdded,
        TurnContext turnContext
    ) {
        /*String welcomeText = "Hello there! Welcome to the bot world!. You can talk to me or text me!";
        return membersAdded.stream()
            .filter(
                member -> !StringUtils
                    .equals(member.getId(), turnContext.getActivity().getRecipient().getId())
            ).map(channel -> turnContext.sendActivity(MessageFactory.text(welcomeText, welcomeText, null)))
            .collect(CompletableFutures.toFutureList()).thenApply(resourceResponses -> null);*/
    	return turnContext.sendActivity(welcomeSuggestedActions()).thenApply(sendResult -> null);
    }
    
    
    public Model createModel(String inputText)
    {	
    	
    	int cashBalance =100;
    	model = new Model();
    	
    	List<Security> securitiesList = new ArrayList<Security>();
    	String allocationRegex = "\\d+[%]+\\s*(?:of\\s*)?\\w+";
    	Pattern pattern = Pattern.compile(allocationRegex,Pattern.DOTALL);
    	Matcher matcher = pattern.matcher(inputText);
    	while(matcher.find())
    	{
    		Security security = new Security();
    		String rawAllocation = matcher.group();
    				
    		String[] rawAllocationSplit = rawAllocation.split("[%]+\\s+(?:of\\s*)?");
    		int allocation = Integer.parseInt(rawAllocationSplit[0]);
    		security.setAllocationPercentage(allocation);
			security.setSecurityName(rawAllocationSplit[1]);
			security.setTicker(SecurityMap.findTicker(rawAllocationSplit[1]));
    		securitiesList.add(security);
    		cashBalance = cashBalance-allocation;
    		System.out.println("*******************************");
    		System.out.println("Securities List: "+securitiesList);
    		System.out.println("cashBalance: "+cashBalance);
    		System.out.println("*******************************");
    	}
    	
    	
    	if(cashBalance<0)
    		return null;
    	/*else if(cashBalance>0)
    	{
    		Security cash = new Security();
    		cash.setAllocationPercentage(cashBalance);
    		cash.setTicker("MONEY");
    		cash.setSecurityName("CASH");
    		securitiesList.add(cash);
    	}*/
    	
    	model.setAllocation(securitiesList);
    	//System.out.println(model);
    	return model;
    	
    }
    
    public Model createIndexFund(String inputText)
    {	
    	//System.out.println(inputText);
    	indexFund = new Model();
    	List<Security> securitiesList = new ArrayList<Security>();
    	String fundNameRegex = "index fund ";
    	
    	String[] rawIndexFundStringSplit = inputText.split(fundNameRegex);
    	if(rawIndexFundStringSplit[1]!=null)
    	{
    		String fundName = rawIndexFundStringSplit[1].replace("\\u0026", "&");
    		Security security = new Security();
    		security.setTicker(SecurityMap.findTicker(fundName));
    		security.setSecurityName(fundName);
    		security.setAllocationPercentage(100);
    		securitiesList.add(security);
    	}
    	
    	indexFund.setAllocation(securitiesList);
    	//System.out.println(indexFund);
    	return indexFund;
    }
    
    
    public Activity sendUrl()
    {
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        activity.setText("You can check the report by [clicking here](https://www.google.com/)");
        activity.setTextFormat(TextFormatTypes.MARKDOWN);
        return activity;
    }
    
    public List<Activity> renderTableandMessage(String text, String tableText)
    {
    	List<Activity> multipleActivites = new ArrayList<Activity>();
    	
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        activity.setText(text);
        activity.setTextFormat(TextFormatTypes.PLAIN);
        
        multipleActivites.add(activity);
        multipleActivites.add(renderTable(tableText));
        
        return multipleActivites;
    }
    
    public Activity renderTable(String text)
    {
    	System.out.println("*********************");
    	System.out.println(text);
    	
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        activity.setText(text);
        activity.setTextFormat(TextFormatTypes.MARKDOWN);
        return activity;
    }
    
    public Activity cardIndividualPerformanceMetrics(String individualPerformanceFileToken)
    {
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        
        //String url = "https://dis-bot-webapplication.azurewebsites.net/api/getIndividualPerformance"
        		String url = "https://dis-web-bot.azurewebsites.net/model.html"
        		//String url = "http://localhost:3978/api/getIndividualPerformance"
        		+ "?individualPerformanceFileToken="+individualPerformanceFileToken;
        
        CardAction cardAction = new CardAction();
        cardAction.setValue(url);
        cardAction.setType(ActionTypes.OPEN_URL);
        
        CardImage imageJPEG = new CardImage();
        imageJPEG.setUrl("https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Stock_market_crash_%282020%29.svg/2880px-Stock_market_crash_%282020%29.svg.png");

        HeroCard heroCard = new HeroCard();
        heroCard.setTitle("Simulated model performance");
        heroCard.setSubtitle("Comparision from 2017 to 2022");
        heroCard.setImages(Collections.singletonList(imageJPEG));
        heroCard.setTap(cardAction);
        
        Attachment attachment = new Attachment();
        attachment.setContentType("application/vnd.microsoft.card.hero");
        attachment.setContent(heroCard);
       
       
        activity.setAttachments(Arrays.asList(attachment));

        return activity;
    }
    
    public Activity cardCombinedPerformanceMetrics(String customModelPerformanceFileToken,String indexFundPerformanceFileToken,String simulatedIndexPerformanceFileToken)
    {
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        
        //String url = "https://dis-bot-webapplication.azurewebsites.net/api/getCombinedPerformance"
        //String url = "http://localhost:3978/api/getCombinedPerformance"
        String url = "https://dis-web-bot.azurewebsites.net/model.html"
        		+ "?customModelPerformanceFileToken="+customModelPerformanceFileToken
        		+ "&indexFundPerformanceFileToken="+indexFundPerformanceFileToken
        		+ "&simulatedIndexPerformanceFileToken="+simulatedIndexPerformanceFileToken;
        
        CardAction cardAction = new CardAction();
        cardAction.setValue(url);
        cardAction.setType(ActionTypes.OPEN_URL);
        
        CardImage imageJPEG = new CardImage();
        imageJPEG.setUrl("https://i.stack.imgur.com/CuvX3.png");

        HeroCard heroCard = new HeroCard();
        heroCard.setTitle("Simulated Direct Indexing Performance");
        heroCard.setSubtitle("Comparision from 2017 to 2022");
        heroCard.setImages(Collections.singletonList(imageJPEG));
        heroCard.setTap(cardAction);
        
        Attachment attachment = new Attachment();
        attachment.setContentType("application/vnd.microsoft.card.hero");
        attachment.setContent(heroCard);
       
       
        activity.setAttachments(Arrays.asList(attachment));

        return activity;
    }
    
    public Activity sendShrugGIF()
    {
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        AnimationCard animationCard = new AnimationCard();
        //animationCard.setTitle("Microsoft Bot Framework");
        //animationCard.setSubtitle("Animation Card");
        //animationCard.setImage(new ThumbnailUrl("https://docs.microsoft.com/en-us/bot-framework/media/how-it-works/architecture-resize.png"));
        animationCard.setMedia(new MediaUrl("https://www.icegif.com/wp-content/uploads/icegif-4589.gif"));
        Attachment attachment = new Attachment();
        attachment.setContentType("application/vnd.microsoft.card.animation");
        attachment.setContent(animationCard);
       
       
        activity.setAttachments(Arrays.asList(attachment));

        return activity;
    }
    
    public Activity welcomeSuggestedActions()
    {
    	Activity activity = MessageFactory.text("Hello there! Welcome to the bot world!. You can talk to me or text me!");
    	
    	CardAction whatCanYouDo = new CardAction();
    	whatCanYouDo.setTitle("What can you do? ????");
    	whatCanYouDo.setType(ActionTypes.IM_BACK);
    	whatCanYouDo.setValue("What can you do?");
    	
    	CardAction shrug = new CardAction();
    	shrug.setTitle("Can you shrug? ????");
    	shrug.setType(ActionTypes.IM_BACK);
    	shrug.setValue("shrug");
    	
    	SuggestedActions actions = new SuggestedActions();
        actions.setActions(Arrays.asList(whatCanYouDo,shrug));
        activity.setSuggestedActions(actions);
        return activity;
        
    }
    
    public Path writeToTempFile(String fileName, String modelPerformace)
    {
    	Path endFilePath = null;
    	try {
    	Path filePath=Files.createTempFile("DIS_BOT_"+fileName, ".json");
    	System.out.println("Temp file Path: " + filePath);
    	System.out.println("Temp file : " + filePath.getFileName());
    	endFilePath = filePath;
    	
    	try (PrintWriter out = new PrintWriter(filePath.toString())) {
    	    out.println(modelPerformace);
    	}
    	
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	return endFilePath;
    }
    
    public String writeToAzureBlob(String fileN, String modelPerformace)
    {
    	String fileToken = "";
    	try {
    	
    		//Not a great idea
    		String connectStr = "DefaultEndpointsProtocol=https;AccountName=disbotblobstorage;AccountKey=237Mp0RXDV430m7qlzOYuIk9iV8LXSMMBC/n4XAIKp6/HgW0rvAF0dfMH3dYIhDCT6ablpmIFcVF+AStROUMKA==;EndpointSuffix=core.windows.net";
    		
    		System.out.println("ConnectionString:"+connectStr);
    		
    		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();
    		String containerName = "disbotperformanceresults"+ java.util.UUID.randomUUID();
    		BlobContainerClient containerClient = blobServiceClient.createBlobContainer(containerName);
    		
    	Path tempPath = writeToTempFile(fileN,modelPerformace);
    	
    	BlobClient blobClient = containerClient.getBlobClient(tempPath.getFileName().toString());
    	System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());
    	blobClient.uploadFromFile(tempPath.toString());

    	fileToken = generateFileToken(containerName,tempPath.getFileName().toString());
    	
    	
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	return fileToken;
    }
    
    public String generateFileToken(String containerName, String fileName)
    {
    	return Base64.getEncoder().encodeToString((containerName+":"+fileName).getBytes());
    }

    
}
