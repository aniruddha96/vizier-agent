/**
 * 
 */
package com.vizier.agent.editor.impl;

import com.vizier.agent.editor.EditorLauncher;

/**
 * @author aniruddha
 * @author sakshimehra
 */
public class LinuxEditorLauncher implements EditorLauncher{

	public boolean openDefaultEditor(String filePath) {
		ProcessBuilder builder = new ProcessBuilder();
		Process p = null;
		try {			
			//Both methods working.
			// String[] command = { "xdg-open", filePath };
			// Process process = new ProcessBuilder(command).start();
			Process process = Runtime.getRuntime().exec(String.format("xdg-open %s", filePath));
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
