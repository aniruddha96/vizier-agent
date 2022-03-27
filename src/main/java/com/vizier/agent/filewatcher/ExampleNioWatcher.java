/**
 * 
 */
package com.vizier.agent.filewatcher;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.vizier.client.VizierBackendClient;

/**
 * @author aniruddha
 *
 */
public class ExampleNioWatcher {
	
	static VizierBackendClient vizierBackendClient;	
	
	public static void main(String[] args) {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get("E:\\test.py");
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			WatchKey key;
			while ((key = watchService.take())!=null) {
				for(WatchEvent<?> event : key.pollEvents()) {
					System.out.println("Modified "+event.kind());
					
					
					//If the event is saved then sync cell
					vizierBackendClient.syncCell();
					
				}
				key.reset();
				
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
