/**
 * 
 */
package com.vizier.agent;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.client.VizierBackendClient;
import com.vizier.directory.DirectoryInitializer;

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
		
		String directoryPath = null ; //parse from args
		String cellIdentifier = null ; //parse from args, String only as a placeholder
		
		directoryInitializer.setUpDirectory(directoryPath);
		directoryInitializer.createIniFile(directoryPath);
		directoryInitializer.getAllStubs(directoryPath, directoryPath);
		
		// 3. Create a new python file
		String pythonFilePath=null;
		
		// 4. Fetch cell contents
		vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath);
		
		// 5. Start file watchers
		
		// 6. Launch editor
		editorLauncher.openDefaultEditor(pythonFilePath);
	}

}
