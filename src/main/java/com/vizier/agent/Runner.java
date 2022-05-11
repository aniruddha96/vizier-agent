/**
 * 
 */
package com.vizier.agent;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.agent.editor.impl.LinuxEditorLauncher;
import com.vizier.agent.editor.impl.MacEditorLauncher;
import com.vizier.agent.editor.impl.WindowsEditorLauncher;
import com.vizier.agent.filewatcher.FileWatcher2;
import com.vizier.agent.filewatcher.FileWatcherHandler;
import com.vizier.agent.filewatcher.FileWatcher;
import com.vizier.client.VizierBackendClient;
import com.vizier.client.VizierBackendClientImpl;
import com.vizier.constants.VizierAgentConstants;
import com.vizier.directory.DirectoryInitializer;
import com.vizier.directory.DirectoryInitializerImpl;
import com.vizier.state.StateHandler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import static com.vizier.constants.VizierAgentConstants.directoryPath;
import static com.vizier.constants.VizierAgentConstants.stubsDirectoryPath;
import static com.vizier.constants.VizierAgentConstants.stubsDirectory;
import static com.vizier.constants.VizierAgentConstants.tempPythonFile;
import static com.vizier.constants.VizierAgentConstants.stateInfoFile;

/**
 * @author aniruddha
 *
 */
/**main class - entry point into the application */
public class Runner {

	DirectoryInitializer directoryInitializer;
	VizierBackendClient vizierBackendClient;
	static EditorLauncher editorLauncher;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// Detecting the OS of the system in use
		System.out.println("Operating system detected : "+System.getProperty("os.name"));
		String os = System.getProperty("os.name").toLowerCase();
		
		/* Create an instance of editor launcher based on the underlying operating system */
		if (os.startsWith("mac")) {
			editorLauncher = new MacEditorLauncher();
		} else if(os.startsWith("linux")){
			editorLauncher = new LinuxEditorLauncher();
		} else {
			editorLauncher = new WindowsEditorLauncher();
		}


		// open cell contents in local editor
		/* Extract the cell action from the arguments and invoke the corresponding methods.*/
		if(args[0].contains("opencell")) {
			new Runner().runner(args);
		}
		// stop syncing the contents of local editor and webUI
		else if(args[0].contains("endcell")) {
			stopAgent(args[0]);
		}	

	}
	
	public static void stopAgent(String url) {

		/*Extract the cell identifier parameters (like projectId, branchId, moduleId) from the argument
		that's passed on in the following format:
		x-vizier-client:opencell/localhost:5000/vizier-db/api/v1/projects/<prjectId>/branches/<branchId>/workflows/<workflowId>/modules/<moduleId>
		x-vizier-client:opencell/localhost:5000/vizier-db/api/v1/projects/1/branches/1/workflows/2/modules/0
		.*/
		List<String> cellIdentifierList = Arrays.asList(url.split("/"));
        String host = cellIdentifierList.get(1);
        String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
        String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
        String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);
        
        String filePath = directoryPath + File.separator
                                + String.join("", host.split(":")) + File.separator
                                + projectId + File.separator
                                + branchId + File.separator 
                                + moduleId + File.separator
								+ "state.json";
								
		/*Update the watcher Active property in state file corresponding to current cell. */					
		File file = new File(filePath);
        if(file.exists()){
            StateHandler.refresh(filePath);
            StateHandler.getState().setWatcherActive(false);
            StateHandler.flush(filePath);
        }
		try {
			String pythonFilePath = directoryPath + File.separator
					+ String.join("", host.split(":")) + File.separator
					+ projectId + File.separator
					+ branchId + File.separator
					+ moduleId + File.separator
					+ "temp.py";
			File pythonFile = new File(filePath);

			/* Append a message at the end of the local python file corresponding to the cell
			indicating that the file is no longer being synced. */
			if(pythonFile.exists()){
				Files.write(Paths.get(pythonFilePath), System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);
				Files.write(Paths.get(pythonFilePath), "#This file is no longer being synced".getBytes(), StandardOpenOption.APPEND);
			}
		}catch (IOException e) {
			System.out.println(e);
		}
	}

	void runner(String[] args) throws IOException {
		// 1. Parse arguments
		String cellIdentifier = args[0]; // parse from args, String only as a placeholder
		
		// 2. Create directory structure for the cell and initialize it
		directoryInitializer = new DirectoryInitializerImpl();
		String cellDirectoryPath = directoryInitializer.setUpDirectory(cellIdentifier);
		// Create the mypy configuration file.
		directoryInitializer.createIniFile(cellDirectoryPath);
		// Create stubs folder in the directory.
		directoryInitializer.getAllStubs("http://localhost:8080/", cellDirectoryPath + stubsDirectory);

		// 3. Create a new python file
		String pythonFilePath = cellDirectoryPath + tempPythonFile;
		directoryInitializer.createTempFile(pythonFilePath);

		// Create a state file that holds the status of file watcher activity on the python file created for
		// the cell.
		String stateFilePath = cellDirectoryPath + stateInfoFile;
		StateHandler.init(cellIdentifier, stateFilePath);

		// 4. Fetch cell contents from Vizier db.
		boolean cellContentsFetched = false;
		try {
			vizierBackendClient = new VizierBackendClientImpl();
			cellContentsFetched = vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

		if(cellContentsFetched){
			// 6. Launch editor once the cell contents are fetched.
			editorLauncher.openDefaultEditor(pythonFilePath);
	
			// 5. Start file watchers
			// FileWatcher.main(pythonFilePath);	
			FileWatcherHandler.start(cellDirectoryPath);
		}
    }
	

}
