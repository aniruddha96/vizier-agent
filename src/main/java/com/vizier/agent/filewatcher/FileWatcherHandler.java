/**
 * 
 */
package com.vizier.agent.filewatcher;

import static com.vizier.constants.VizierAgentConstants.directoryPath;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * @author aniruddha
 *
 */
public class FileWatcherHandler {

	public static FileAlterationMonitor monitor;
	
	public static FileAlterationMonitor getMonitor() {
		return monitor;
	}
	public static void start(String directoryPath) {
		FileAlterationObserver observer = new FileAlterationObserver(directoryPath);
         monitor = new FileAlterationMonitor(500);

        FileWatcher2 lis = new FileWatcher2();
        observer.addListener(lis);
        monitor.addObserver(observer);
        try {
			monitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
