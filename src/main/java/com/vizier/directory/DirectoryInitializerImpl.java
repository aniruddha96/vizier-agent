package com.vizier.directory;

import com.vizier.stub.client.VizierStubServerClient;
import com.vizier.stub.client.VizierStubServerClientImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.FileCopyUtils;

import static com.vizier.constants.VizierAgentConstants.directoryPath;
import static com.vizier.constants.VizierAgentConstants.iniFilePath;
import static com.vizier.constants.VizierAgentConstants.stubsDirectory;
import static com.vizier.constants.VizierAgentConstants.iniFile;

public class DirectoryInitializerImpl implements DirectoryInitializer{

    public String setUpDirectory(String cellIdentifier) {
        try{
            List<String> cellIdentifierList = Arrays.asList(cellIdentifier.split("/"));
            String host = cellIdentifierList.get(1); //TODO: Change this
            String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
            String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
            String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);
            
            File file = new File(directoryPath + File.separator
                                    + String.join("", host.split(":")) + File.separator
                                    + projectId + File.separator
                                    + branchId + File.separator 
                                    + moduleId + File.separator
                                    );
            if(!file.exists()){
                file.mkdirs();
            }
            return file.getPath() + File.separator;
        }
        catch(Exception e){
            System.out.println("An exception occured during Directory setup.");
            e.printStackTrace();
        }
        return "";
    }

    public boolean createIniFile(String directoryPath) {
        try {
            File file = new File(directoryPath + iniFile);
            // if (file.createNewFile()) {
            //   System.out.println("File created: " + file.getName());
            //   FileWriter myWriter = new FileWriter(iniFilePath);
            //   myWriter.write("[mypy]\n\nmypy_path=stubs");
            //   myWriter.append("\n\n[flake8]");
            //   myWriter.close();
            //   System.out.println("Successfully wrote to the file.");
            
            if(!file.exists()){
                InputStream configFileStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(iniFile);
                FileCopyUtils.copy(configFileStream, new FileOutputStream(file.getPath()));
                System.out.println("Config File created.");
            } else {
              System.out.println("Config file already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
          }
        return true;
    }

    public boolean getAllStubs(String serverAddress, String extractTo) {
        VizierStubServerClient stubServerClient = new VizierStubServerClientImpl();
        try {
            return stubServerClient.getAllStubs(serverAddress, extractTo);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createTempFile(String filePath) {
        try {
            File myObj = new File(filePath);
            
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

} 