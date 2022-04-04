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
import com.vizier.constants.VizierAgentConstants;
import com.vizier.directory.DirectoryInitializer;
import com.vizier.directory.DirectoryInitializerImpl;
import com.vizier.state.StateHandler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import static com.vizier.constants.VizierAgentConstants.directoryPath;
import static com.vizier.constants.VizierAgentConstants.stubsDirectoryPath;

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
		args = new String[] { "temp.py" };
		new Runner().runner(args);

	}

	void runner(String[] args) throws IOException {
		// 1. Parse args
		// 2. Create directory and initialize it
		String cellIdentifier = null; // parse from args, String only as a placeholder
		directoryInitializer = new DirectoryInitializerImpl();
		directoryInitializer.setUpDirectory();
		directoryInitializer.createIniFile();
		//directoryInitializer.getAllStubs("http://localhost:8080/", stubsDirectoryPath);

		StateHandler.init("");
		// 3. Create a new python file
		String[] pythonFilePath = { VizierAgentConstants.directoryPath + File.separator + args[0] };
		directoryInitializer.createTempFile(pythonFilePath[0]);
		// 4. Fetch cell contents
		// vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath[0]);

		// 6. Launch editor
		editorLauncher.openDefaultEditor(pythonFilePath[0]);

		// 5. Start file watchers
		// FileWatcher.main(pythonFilePath);

		FileWatcherHandler.start(directoryPath);
    }
	

}
