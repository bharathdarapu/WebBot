// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.ms.cloudathon2022;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ActivityTypes;
import com.microsoft.bot.schema.Attachment;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.CardImage;
import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.ConversationParameters;
import com.microsoft.bot.schema.ConversationResourceResponse;
import com.microsoft.bot.schema.HeroCard;
import com.microsoft.bot.schema.ResourceResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * This class implements the functionality of the Bot.
 *
 * <p>
 * This is where application specific logic for interacting with the users would be added. For this
 * sample, the {@link #onMessageActivity(TurnContext)} echos the text back to the user. The {@link
 * #onMembersAdded(List, TurnContext)} will send a greeting to new conversation participants.
 * </p>
 */
public class EchoBot extends ActivityHandler {

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
    	
    	//return turnContext.sendActivity(sendCardToConversation()).thenApply(sendResult -> null);
    	
        return turnContext.sendActivity(
            MessageFactory.text("You said: " + turnContext.getActivity().getText())
        ).thenApply(sendResult -> null);
    }

    @Override
    protected CompletableFuture<Void> onMembersAdded(
        List<ChannelAccount> membersAdded,
        TurnContext turnContext
    ) {
        String welcomeText = "Hello World!";
        return membersAdded.stream()
            .filter(
                member -> !StringUtils
                    .equals(member.getId(), turnContext.getActivity().getRecipient().getId())
            ).map(channel -> turnContext.sendActivity(MessageFactory.text(welcomeText, welcomeText, null)))
            .collect(CompletableFutures.toFutureList()).thenApply(resourceResponses -> null);
    }

    public Activity sendCardToConversation()
    {
    	Activity activity = new Activity(ActivityTypes.MESSAGE);
        activity.setRecipient(activity.getRecipient());
        activity.setFrom(activity.getFrom());
        
        CardAction cardAction = new CardAction();
        cardAction.setValue("https://www.google.com/");
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
        
        ConversationParameters createMessage = new ConversationParameters();
        //createMessage.setMembers(Collections.singletonList(user));
        //createMessage.setBot(bot);

        return activity;
    }
   

}
