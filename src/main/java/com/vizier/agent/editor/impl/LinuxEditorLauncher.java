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
			//TODO: Add open system call for linux
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
