/**
 * 
 */
package com.vizier.agent.filewatcher;

import java.io.FileReader;
import java.io.FileWriter;
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

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/**
 * @author aniruddha
 * @author sakshimehra
 */
public class FileWatcher {
	
	static VizierBackendClient vizierBackendClient;	
	
	public static void main(String[] args) {
		String path = args[0];
		String dir = path.substring(0,path.lastIndexOf("/"));
		String filename = path.substring(path.lastIndexOf("/")+1);
		try {
			// Creates a instance of WatchService.
			WatchService watcher = FileSystems.getDefault().newWatchService();

			// Registers the logDir below with a watch service.
			Path logDir = Paths.get(dir);
			logDir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

			// Monitor the logDir at listen for change notification.
			while (true) {
				WatchKey key = watcher.take();
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					if (ENTRY_CREATE.equals(kind)) {
						System.out.println("Entry was created on log dir.");
					} else if (ENTRY_MODIFY.equals(kind)) {
						System.out.println("Entry was modified on log dir.");
						System.out.println(
								"Event kind:" + event.kind()
										+ ". File affected: " + event.context() + ".");
						try {
							// Creating a FileReader object
							FileReader fr = new FileReader(path+"/"+filename);

							// Creating a FileWriter object
							FileWriter fw = new FileWriter("state.txt");

							// Declaring a blank string in which
							// whole content of file is to be stored
							String str = "";

							int i;

							while ((i = fr.read()) != -1) {

								// Storing every character in the string
								str += (char)i;
							}

							// Print and display the string that
							// contains file data
							System.out.println(str);

							// Writing above string data to
							// FileWriter object
							fw.write(str);

							fr.close();
							fw.close();

							// Display message
							System.out.println(
									"File reading and writing both done");
                        }
                        catch (IOException e) {
                            // Display message
                            System.out.println(
                                    "There are some IOException");
                        }
					} else if (ENTRY_DELETE.equals(kind)) {
						System.out.println("Entry was deleted from log dir.");
					}
				}
				key.reset();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
