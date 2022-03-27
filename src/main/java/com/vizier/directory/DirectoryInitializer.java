/**
 * 
 */
package com.vizier.directory;

import com.vizier.stub.client.VizierStubServerClient;
/**
 * @author aniruddha
 *
 */
public interface DirectoryInitializer {

	public boolean setUpDirectory(String directoryPath);
	
	public boolean createIniFile(String directoryPath);
	
	public boolean getAllStubs(String serverAddress, String extractTo);
}
