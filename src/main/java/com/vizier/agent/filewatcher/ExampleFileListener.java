/**
 * 
 */
package com.vizier.agent.filewatcher;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.vizier.client.VizierBackendClient;

/**
 * @author aniruddha
 *
 */
public class ExampleFileListener implements FileAlterationListener{

	VizierBackendClient vizierBackendClient;	
	
	public void onStart(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

	public void onDirectoryCreate(File directory) {
		// TODO Auto-generated method stub
		
	}

	public void onDirectoryChange(File directory) {
		// TODO Auto-generated method stub
		
	}

	public void onDirectoryDelete(File directory) {
		// TODO Auto-generated method stub
		
	}

	public void onFileCreate(File file) {
		// TODO Auto-generated method stub
		
	}

	public void onFileChange(File file) {
		// TODO Auto-generated method stub
		vizierBackendClient.syncCell();
		
	}

	public void onFileDelete(File file) {
		// TODO Auto-generated method stub
		
	}

	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

}
