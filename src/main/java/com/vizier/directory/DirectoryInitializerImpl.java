package com.vizier.directory;

import com.vizier.stub.client.VizierStubServerClient;
import com.vizier.stub.client.VizierStubServerClientImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.FileCopyUtils;

import static com.vizier.constants.VizierAgentConstants.*;

public class DirectoryInitializerImpl implements DirectoryInitializer {

    public String setUpDirectory(String cellIdentifier) {
        try {
            /*Creates a directory structure for a cell identified by @cellIdentifier which has
            the format: x-vizier-client:opencell/localhost:5000/vizier-db/api/v1/projects/<projectId>/branches/<branchId>/workflows/<workflowId>/modules/<moduleId> */

            //Extract the cell identifier parameters from @cellIdentifier
            List<String> cellIdentifierList = Arrays.asList(cellIdentifier.split("/"));
            String host = cellIdentifierList.get(1); //TODO: Change this
            String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
            String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
            String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);

            File file = new File(directoryPath + File.separator
                    + String.join("", host.split(":")) + File.separator
                    + projectId + File.separator
                    + branchId + File.separator
                    + moduleId + File.separator);
            // Create a directory structure indicated by the file path if it does not exist.
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getPath() + File.separator;
        } catch (Exception e) {
            System.out.println("An exception occured during Directory setup.");
            e.printStackTrace();
        }
        return "";
    }

    public boolean createIniFile(String directoryPath) {
        try {
            /*Creates a mypy.ini configuration file in @directoryPath */
            File file = new File(directoryPath + iniFilePath1);
            if(!file.exists()){
                InputStream configFileStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(iniFilePath1);
                FileCopyUtils.copy(configFileStream, new FileOutputStream(file.getPath()));
                configFileStream.close();
                System.out.println("Mypy ini File created.");
            } else {
              System.out.println("Mypy ini file already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
          }
        return true;
    }

    public boolean getAllStubs(String serverAddress, String extractTo) {
        /* Requests stubs from a StubServer running at @serverAddrss and extracts them to
        a directory identified by @extractTo path.
        */
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
            /*Creates a python file at @filePath with the name "temp.p" */
            File myObj = new File(filePath);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter myWriter = new FileWriter(filePath);

                // Add a statement to import vizier related stubs which helps in linting the python file
                // based on Vizier related stubs present in pycell folder inside stubs folder.
                myWriter.write("from pycell.client import vizierdb #Do not delete this line");
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
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