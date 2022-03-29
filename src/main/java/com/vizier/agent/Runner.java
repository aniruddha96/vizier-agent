/**
 * 
 */
package com.vizier.agent;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.agent.editor.impl.WindowsEditorLauncher;
import com.vizier.client.VizierBackendClient;
import com.vizier.constants.VizierAgentConstants;
import com.vizier.directory.DirectoryInitializer;
import com.vizier.directory.DirectoryInitializerImpl;

import java.io.File;

import static com.vizier.constants.VizierAgentConstants.directoryPath;
import static com.vizier.constants.VizierAgentConstants.stubsDirectoryPath;

/**
 * @author aniruddha
 *
 */
public class Runner {

	DirectoryInitializer directoryInitializer;
	VizierBackendClient vizierBackendClient;
	EditorLauncher editorLauncher;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Runner().runner(args);

	}

	void runner(String[] args) {
		// 1. Parse args
		// 2. Create directory and initialize it
		String cellIdentifier = null ; //parse from args, String only as a placeholder
		directoryInitializer = new DirectoryInitializerImpl();
		directoryInitializer.setUpDirectory();
		directoryInitializer.createIniFile();
		directoryInitializer.getAllStubs(directoryPath, stubsDirectoryPath);
		
		// 3. Create a new python file
		String pythonFilePath=null;
		
		// 4. Fetch cell contents
		vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath);
		
		// 5. Start file watchers
		
		// 6. Launch editor
		editorLauncher = new WindowsEditorLauncher();
		//Below abc.xyz file path will be changed to temp python file
		editorLauncher.openDefaultEditor(VizierAgentConstants.directoryPath+ File.separator+"abc.xyz");
	}

}
