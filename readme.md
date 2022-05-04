Deploy BOT

az login --use-device-code

[old] az account set --subscription "b54acde2-eb6d-488b-8439-066dbd54def8"
[NEW] az account set --subscription "76d7b4be-2d57-4810-bb9b-f08d8ba1cfb4"

az ad app create --display-name "DIS-Bot" --password "AtLeastSixteenCharacters_0" --available-to-other-tenants

az deployment group create --resource-group "DIS-RG" --template-file "/Users/bdarapu/Projects/Cloudathon2022/BotFramework/echo/deploymentTemplates/template-with-preexisting-rg.json" --parameters appId="76100fe7-a5bf-4c3f-b1dd-dee6426206cb" appSecret="AtLeastSixteenCharacters_0" appServicePlanLocation="eastus" botId="DIS-Bot" newWebAppName="DIS-Bot-WebApplication" newAppServicePlanName="DIS-Bot-ServicePlan" --name "DIS-Bot"

mvn azure-webapp:deploy -Dbotname="DIS-Bot-WebApplication"



curl -v -X POST "https://eastus.api.cognitive.microsoft.com/sts/v1.0/issueToken" -H "Ocp-Apim-Subscription-Key: 76d7b4be-2d57-4810-bb9b-f08d8ba1cfb4" -H "Content-type: application/x-www-form-urlencoded" -H "Content-Length: 0"

curl -v -X POST \
"https://eastus.api.cognitive.microsoft.com/sts/v1.0/issueToken" \
-H "Content-type: application/x-www-form-urlencoded" \
-H "Content-length: 0" \
-H "Ocp-Apim-Subscription-Key: 665ba4ced3344ffdb4e2bcb969854b91"


curl -v -X POST \
"https://YOUR-REGION.api.cognitive.microsoft.com/sts/v1.0/issueToken" \
-H "Content-type: application/x-www-form-urlencoded" \
-H "Content-length: 0" \	
-H "Ocp-Apim-Subscription-Key: YOUR_SUBSCRIPTION_KEY"



curl -v -X POST \
"https://directline.botframework.com/v3/directline/tokens/generate" \
-H "Content-length: 0" \
-H "Authorization: Bearer Q513mka7E8c.4oNFSsD0EXD7Qq5LfbcQLt2MKuAiqDwVvc_283-2s4k"


Speech to Text
Key 1: 665ba4ced3344ffdb4e2bcb969854b91
Key 2: a023211667fe4fcbab3ab1bbf81de1d6


Deploying the webapp:
cd /Users/bdarapu/Projects/Cloudathon2022/BotFramework/SpeechClient/html-docs-hello-world
az webapp up --location eastus --resource-group "DIS-RG" --name DIS-Web-Bot --html

6788

% increase = ((New - Old) ÷ Old) × 100.

((4000-3000)/3000)*100


http://localhost:3978/api/getIndividualPerformance?fileName=DIS_BOT_customModelPerformace2960263412983348002.json

http://localhost:3978/api/getCombinedPerformance?customModelPerformanceFile=DIS_BOT_customModelPerformace2960263412983348002.json&indexFundPerformanceFile=DIS_BOT_indexFundPerformace2594140875825323877.json&simulatedIndexPerformanceFile=DIS_BOT_simulatedModelPerformance4186813840724856026.json

http://localhost:3978/api/getCombinedPerformance?customModelPerformanceFile=DIS_BOT_customModelPerformace3506079767381165281.json&indexFundPerformanceFile=DIS_BOT_indexFundPerformace6564710828809481638.json&simulatedIndexPerformanceFile=DIS_BOT_simulatedModelPerformance4981804512485018624.json



Azure blob:
export AZURE_STORAGE_CONNECTION_STRING="DefaultEndpointsProtocol=https;AccountName=disbotblobstorage;AccountKey=13F3dCXfgPA070FGM9kFORqOkEdLLQUE+BHStXboso6oS8lF+4ZhooqJOj79A/B+PkCgUekQdiJj+AStbZabQA==;EndpointSuffix=core.windows.net"


http://localhost:3978/api/getCombinedPerformance
?customModelPerformanceFileToken=ZGlzYm90cGVyZm9ybWFuY2VyZXN1bHRzOWMyZDExMjItMjFlOS00ZWNlLWE2ZDYtYjEzNDllOGMyNmI3OkRJU19CT1RfY3VzdG9tTW9kZWxQZXJmb3JtYWNlMzMwMjk2MTMxMTAxMjc2OTQ0Mi5qc29u&indexFundPerformanceFileToken=ZGlzYm90cGVyZm9ybWFuY2VyZXN1bHRzNGUwY2UwYjAtYmQ1Ni00MDYxLWE0NDItN2ZjZWRhMDAxYzNiOkRJU19CT1RfaW5kZXhGdW5kUGVyZm9ybWFjZTMwODYyMDUxNjU0NzE0MjU3MjMuanNvbg==
&simulatedIndexPerformanceFileToken=ZGlzYm90cGVyZm9ybWFuY2VyZXN1bHRzMTRjODY4MTktOWUzMC00YjgyLTkwM2EtMGZjMGExNDQ5MGRmOkRJU19CT1Rfc2ltdWxhdGVkTW9kZWxQZXJmb3JtYW5jZTQxOTk3Mjc5MDM4NDczMjU5ODIuanNvbg==


https://dis-bot-webapplication.azurewebsites.net/test


https://dis-bot-webapplication.azurewebsites.net/api/getIndividualPerformance?individualPerformanceFileToken=ZGlzYm90cGVyZm9ybWFuY2VyZXN1bHRzYjA4M2E2MTgtZWI1YS00ZWQxLWE0ZjMtZDQ1ZTc1NmU2OTJiOkRJU19CT1RfaW5kZXhGdW5kUGVyZm9ybWFjZTQyMDI4NDk5ODMzMDU3MTYwMjkuanNvbg==



- you can ask to -- what can you do?
-- balance will be taken as cash 


create a model with 15% amazon, 15% of Google 25% of Apple and 35% of IBM
get index fund DOW



