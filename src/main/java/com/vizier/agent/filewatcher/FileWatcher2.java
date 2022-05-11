/**
 * 
 */
package com.vizier.agent.filewatcher;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.vizier.client.VizierBackendClient;
import com.vizier.client.VizierBackendClientImpl;
import com.vizier.state.StateHandler;
import com.vizier.state.State;

/**
 * @author aniruddha
 *
 */
/** Filewatcher to look for changes in the locally opened file and uses state.json to stop watching the file */
public class FileWatcher2 extends FileAlterationListenerAdaptor{

	@Override
	public void onFileChange(final File file) {
		if(file.getName().contentEquals("state.json")) {
			try {
				StateHandler.refresh(file.getPath());
				if(!StateHandler.getState().isWatcherActive()) {
					System.out.println("State json alerted to stop watching file");
					FileWatcherHandler.getMonitor().stop();
				}			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(StateHandler.getState().isWatcherActive() && file.getName().contentEquals("temp.py")) {
			System.out.println("Python file changes, syncing changes with backend.");
			VizierBackendClient vizierBackendClient = new VizierBackendClientImpl();
			vizierBackendClient.syncCell(StateHandler.getState().getCellIdentifier(), file.getAbsolutePath());
		}
	}

}
