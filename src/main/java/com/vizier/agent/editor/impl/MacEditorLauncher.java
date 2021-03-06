/**
 * 
 */
package com.vizier.agent.editor.impl;

import com.vizier.agent.editor.EditorLauncher;

import java.io.IOException;

/**
 * @author aniruddha
 * @author sakshimehra
 */
/** MacEditorLauncher class implementing the EditorLauncher interface for Mac OS*/
public class MacEditorLauncher implements EditorLauncher{

	public boolean openDefaultEditor(String filePath) throws IOException {
			ProcessBuilder builder = new ProcessBuilder();
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(String.format("open %s", filePath));
				return true;
			} catch (Exception e) {
				p = Runtime.getRuntime().exec(String.format("open -a TextEdit %s", filePath));
				System.out.println(e.getMessage());
				return false;
			}
	}

}
