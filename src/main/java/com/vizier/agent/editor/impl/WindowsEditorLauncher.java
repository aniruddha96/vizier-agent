/**
 * 
 */
package com.vizier.agent.editor.impl;

import com.vizier.agent.editor.EditorLauncher;
import com.vizier.constants.VizierAgentConstants;

import java.awt.*;
import java.io.File;

/**
 * @author aniruddha
 *
 */
public class WindowsEditorLauncher implements EditorLauncher{

	public boolean openDefaultEditor(String filePath) {
		try
		{
			File file = new File(filePath);
			if(!Desktop.isDesktopSupported())
			{
				Runtime.getRuntime().exec(new String[] {"cmd.exe", "/C", filePath});

				return false;
			}
			Desktop desktop = Desktop.getDesktop();
			if(file.exists()) {
				desktop.open(file);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
