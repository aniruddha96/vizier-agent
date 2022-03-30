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

	public boolean setUpDirectory();
	
	public boolean createIniFile();
	
	public boolean getAllStubs(String serverAddress, String extractTo);

	public boolean createTempFile(String filePath);
}
