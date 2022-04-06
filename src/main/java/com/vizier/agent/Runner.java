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
import static com.vizier.constants.VizierAgentConstants.stateInfoFile;;

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
		args = new String[] {"x-vizier-client:opencell/localhost:5000/vizier-db/api/v1/projects/1/branches/1/workflows/5/modules/0"};
		new Runner().runner(args);

	}

	void runner(String[] args) throws IOException {
		// 1. Parse args
		String cellIdentifier = args[0]; // parse from args, String only as a placeholder
		
		// 2. Create directory and initialize it
		directoryInitializer = new DirectoryInitializerImpl();
		String cellDirectoryPath = directoryInitializer.setUpDirectory(cellIdentifier);
		directoryInitializer.createIniFile(cellDirectoryPath);
		directoryInitializer.getAllStubs("http://localhost:8080/", cellDirectoryPath + stubsDirectory);
		StateHandler.init(cellDirectoryPath + stateInfoFile);

		// 3. Create a new python file
		String pythonFilePath = cellDirectoryPath + tempPythonFile;
		directoryInitializer.createTempFile(pythonFilePath);

		// 4. Fetch cell contents
		// Check if the below can be coded into a method call
		boolean cellContentsFetched = false;
		try {
			VizierBackendClient vizierBackendClient = new VizierBackendClientImpl();
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
