package com.vizier.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            return true;
        }
        catch(Exception e){
            System.out.println("An Exception occurred while trying to fetch cell contents from Vizierdb");
            e.printStackTrace();
        }
        return false;
    }

    public boolean syncCell(){
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
