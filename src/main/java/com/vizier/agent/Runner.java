/**
 * 
 */
package com.vizier.agent;

import com.vizier.agent.editor.EditorLauncher;
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
public class Runner {

	DirectoryInitializer directoryInitializer;
	VizierBackendClient vizierBackendClient;
	static EditorLauncher editorLauncher;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac");
		if (isMac) {
			editorLauncher = new MacEditorLauncher();
		} else {
			editorLauncher = new WindowsEditorLauncher();
		}
		// TODO: Add case for linux OS

		// TODO: Add case for Windows OS
		// args = new String[] { "temp.py" };
		//args = new String[] {"x-vizier-client:opencell/localhost:5000/vizier-db/api/v1/projects/1/branches/1/workflows/13/modules/0"};
		
		if(args[0].contains("opencell")) {
			new Runner().runner(args);
		}else if(args[0].contains("endcell")) {
			stopAgent(args[0]);
		}
		

	}
	
	public static void stopAgent(String url) {
		List<String> cellIdentifierList = Arrays.asList(url.split("/"));
        String host = cellIdentifierList.get(1); //TODO: Change this
        String projectId = cellIdentifierList.get(cellIdentifierList.indexOf("projects") + 1);
        String branchId = cellIdentifierList.get(cellIdentifierList.indexOf("branches") + 1);
        String moduleId = cellIdentifierList.get(cellIdentifierList.indexOf("modules") + 1);
        
        String filePath = directoryPath + File.separator
                                + String.join("", host.split(":")) + File.separator
                                + projectId + File.separator
                                + branchId + File.separator 
                                + moduleId + File.separator
                                + "state.json";
        File file = new File(filePath);
        if(file.exists()){
            StateHandler.refresh(filePath);
            StateHandler.getState().setWatcherActive(false);
            StateHandler.flush(filePath);
        }
	}

	void runner(String[] args) throws IOException {
		// 1. Parse args
		String cellIdentifier = args[0]; // parse from args, String only as a placeholder
		
		// 2. Create directory and initialize it
		directoryInitializer = new DirectoryInitializerImpl();
		String cellDirectoryPath = directoryInitializer.setUpDirectory(cellIdentifier);
		directoryInitializer.createIniFile(cellDirectoryPath);
		directoryInitializer.getAllStubs("http://localhost:8080/", cellDirectoryPath + stubsDirectory);

		String stateFilePath = cellDirectoryPath + stateInfoFile;
		StateHandler.init(cellIdentifier, stateFilePath);

		// 3. Create a new python file
		String pythonFilePath = cellDirectoryPath + tempPythonFile;
		directoryInitializer.createTempFile(pythonFilePath);

		// 4. Fetch cell contents
		// Check if the below can be coded into a method call
		boolean cellContentsFetched = false;
		try {
			vizierBackendClient = new VizierBackendClientImpl();
			cellContentsFetched = vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

		if(cellContentsFetched){
			// 6. Launch editor
			editorLauncher.openDefaultEditor(pythonFilePath);
	
			// 5. Start file watchers
			// FileWatcher.main(pythonFilePath);	
			FileWatcherHandler.start(cellDirectoryPath);
		}
    }
	

}
