/**
 * 
 */
package com.vizier.agent;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.agent.editor.impl.MacEditorLauncher;
import com.vizier.agent.filewatcher.FileWatcher;
import com.vizier.client.VizierBackendClient;
import com.vizier.directory.DirectoryInitializer;

import java.io.IOException;

/**
 * @author aniruddha
 * @author sakshimehra
 */
public class Runner {

	DirectoryInitializer directoryInitializer;
	VizierBackendClient vizierBackendClient;
	static EditorLauncher editorLauncher;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		boolean isMac = System.getProperty("os.name")
				.toLowerCase().startsWith("mac");
		if(isMac){
			editorLauncher = new MacEditorLauncher();
		}
		// TODO: Add case for linux OS

		//TODO: Add case for Windows OS

		new Runner().runner(args);

	}

	void runner(String[] args) throws IOException {
		// 1. Parse args
		// 2. Create directory and initialize it
		
		String directoryPath = null ; //parse from args
		String cellIdentifier = null ; //parse from args, String only as a placeholder
		
		directoryInitializer.setUpDirectory(directoryPath);
		directoryInitializer.createIniFile(directoryPath);
		directoryInitializer.getAllStubs(directoryPath, directoryPath);
		
		// 3. Create a new python file
		String[] pythonFilePath=null;
		
		// 4. Fetch cell contents
		vizierBackendClient.fetchCellContentTo(cellIdentifier, pythonFilePath[0]);
		
		// 5. Start file watchers
		FileWatcher.main(pythonFilePath);

		// 6. Launch editor
		editorLauncher.openDefaultEditor(pythonFilePath[0]);
	}

}
