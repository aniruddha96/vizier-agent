package com.vizier.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** fetches cell contents to the given filepath */
public class VizierBackendClientImpl implements VizierBackendClient {
    public boolean fetchCellContentTo(String cellIdentifier, String filePath) throws URISyntaxException {
        try{
            List<String> cellIdentifierList = new ArrayList<String>(Arrays.asList(cellIdentifier.split("/")));
            cellIdentifierList.remove(0); //TODO: Check if this can be done in a better way.
            String url = "http://" + String.join("/", cellIdentifierList);
            
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = client.execute(request);
            // System.out.println(response.body());

            if(response.getStatusLine().getStatusCode() == 200){
            	HttpEntity entity = response.getEntity();
                ParseResponseToFile(EntityUtils.toString(entity), filePath);
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not fetch cell contents from Vizierdb." + response);
                return false;
            }
            return true;
        }
        catch(Exception e){
            System.out.println("An Exception occurred while trying to fetch cell contents from Vizierdb");
            e.printStackTrace();
        }
        return false;
    }
/** Snyc cell contents to vizierdb */
    public boolean syncCell(String cellIdentifier, String filePath){
        try{          
            List<String> cellIdentifierList = new ArrayList<String>(Arrays.asList(cellIdentifier.split("/")));
            cellIdentifierList.remove(0); //TODO: Check if this can be done in a better way.
            String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
            String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
            String currWfId = cellIdentifierList.get(cellIdentifierList.indexOf("workflows") + 1);
            String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);

            //Fetch latest workflowId from Vizierdb by making a head call
            int latestwfId = Integer.parseInt(currWfId);
            String url = String.format("http://localhost:5000/vizier-db/api/v1/projects/%s/branches/%s/head", projectId, branchId);
            String postURL = "";
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet   request = new HttpGet (url);
            CloseableHttpResponse response = client.execute(request);
            
            // System.out.println(response.body());

            if(response.getStatusLine().getStatusCode() == 200){
            	HttpEntity entity = response.getEntity();
                JSONObject responseObject = new JSONObject(EntityUtils.toString(entity));
                latestwfId = Integer.parseInt(responseObject.getString("id"));
                //JSONArray linksArray = responseObject.getJSONArray("links");
                //postURL = linksArray.getJSONObject(0).getString("href");
                postURL = String.format("http://localhost:5000/vizier-db/api/v1/projects/%s/branches/%s/workflows/%s/modules/%s", projectId, branchId, latestwfId, moduleId);
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not fetch workflowID from Vizierdb." + response);
                return false;
            }

            //Make a PUT Request to Vizierdb with the updated cell contents.
            //Construct request body.
            JSONObject requestObject = new JSONObject();
            requestObject.put("packageId", "script");
            requestObject.put("commandId", "python");
            JSONArray argArray = new JSONArray();
            JSONObject dataObj = new JSONObject();
            dataObj.put("id", "source");
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            boolean firstLine = true;
            StringBuilder builder = new StringBuilder();
            while (myReader.hasNextLine()) {
                if(firstLine){
                    myReader.nextLine();
                    firstLine = false;
                    continue;
                }
                builder.append(myReader.nextLine());
                builder.append(System.getProperty("line.separator"));
            }
            myReader.close();
            dataObj.put("value", builder.toString());
            //dataObj.put("value", Files.readString(Paths.get(filePath)));
            argArray.put(dataObj);
            requestObject.put("arguments", argArray);
            
            //Make a PUT call to save the data in backend.
            HttpPut putRequest = new HttpPut(postURL);
            
            putRequest.setEntity(new StringEntity(requestObject.toString()));
            CloseableHttpResponse putResponse = client.execute(putRequest);
            // System.out.println(response.body());

            if(putResponse.getStatusLine().getStatusCode() == 200){
                System.out.println("Successfully updated cell contents to Vizierdb." + putResponse);
                return true;
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not sync cell contents to Vizierdb." + putResponse);
                return false;
            }
        }
        catch(Exception e){
            System.out.println("An Exception occurred while trying to sync cell contents to Vizierdb");
            e.printStackTrace();
        }
        return false;
    }

    public static void ParseResponseToFile(String response, String filePath){
        try{
            JSONObject responseObject = new JSONObject(response);
            String cellContent = responseObject.getString("text");
            File file = new File(filePath);
            if(!file.exists() && !file.isDirectory()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(filePath);
            writer.write("from stubs.pycell.client import vizierdb #Do not delete this line\n");
            writer.write(cellContent);
            writer.close();

            System.out.println("Successfully written cell contents to the file.");
        }catch(JSONException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
