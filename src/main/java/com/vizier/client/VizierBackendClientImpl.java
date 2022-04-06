package com.vizier.client;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.*;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static void ParseResponseToFile(){
        
    }
    
}
