package com.vizier.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class VizierBackendClientImpl implements VizierBackendClient {
    public boolean fetchCellContentTo(String cellIdentifier, String filePath) throws URISyntaxException {
        try{
            List<String> cellIdentifierList = new ArrayList<String>(Arrays.asList(cellIdentifier.split("/")));
            cellIdentifierList.remove(0); //TODO: Check if this can be done in a better way.
            String url = "http://" + String.join("/", cellIdentifierList);
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println(response.body());

            if(response.statusCode() == 200){
                ParseResponseToFile(response.body(), filePath);
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not fetch cell contents from Vizierdb." + response.body());
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

    public boolean syncCell(String cellIdentifier, String filePath){
        try{          
            List<String> cellIdentifierList = new ArrayList<String>(Arrays.asList(cellIdentifier.split("/")));
            cellIdentifierList.remove(0); //TODO: Check if this can be done in a better way.
            String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
            String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
            String currWfId = cellIdentifierList.get(cellIdentifierList.indexOf("workflows") + 1);
            String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);

            //Fetch latest workflowId from Vizierdb
            int latestwfId = Integer.parseInt(currWfId);
            String url = String.format("http://localhost:5000/vizier-db/api/v1/projects/%s/branches/%s/head", projectId, branchId);
            String postURL = "";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                            .uri(URI.create(url))
                                            .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println(response.body());

            if(response.statusCode() == 200){
                JSONObject responseObject = new JSONObject(response.body());
                latestwfId = Integer.parseInt(responseObject.getString("id"));
                //JSONArray linksArray = responseObject.getJSONArray("links");
                //postURL = linksArray.getJSONObject(0).getString("href");
                postURL = String.format("http://localhost:5000/vizier-db/api/v1/projects/%s/branches/%s/workflows/%s/modules/%s", projectId, branchId, latestwfId, moduleId);
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not fetch workflowID from Vizierdb." + response.body());
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
                builder.append(myReader.nextLine()+"\n");
            }
            myReader.close();
            dataObj.put("value", builder.toString());
            //dataObj.put("value", Files.readString(Paths.get(filePath)));
            argArray.put(dataObj);
            requestObject.put("arguments", argArray);
            
            //Make a PUT call to save the data in backend.
            request = HttpRequest.newBuilder()
                                .uri(URI.create(postURL))
                                .headers("Content-Type", "application/json")
                                .PUT(HttpRequest.BodyPublishers.ofString(requestObject.toString()))
                                .build();
            response = client.send(request, BodyHandlers.ofString());
            System.out.println(response.body());

            if(response.statusCode() == 200){
                System.out.println("Successfully updated cell contents to Vizierdb." + response.body());
                return true;
            }
            else{
                //TODO: Handle failure response.
                System.out.println("Could not sync cell contents to Vizierdb." + response.body());
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
