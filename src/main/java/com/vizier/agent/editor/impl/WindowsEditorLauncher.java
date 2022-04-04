/**
 * 
 */
package com.vizier.agent.editor.impl;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.constants.VizierAgentConstants;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author aniruddha
 *
 */
public class WindowsEditorLauncher implements EditorLauncher{

	public boolean openDefaultEditor(String filePath) {
		try
		{ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", filePath);
		try {
		    Process p = pb.start();
		    long pid = p.pid();
		    System.out.println(pid);
		} catch (IOException ex) {
		    // ...
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
