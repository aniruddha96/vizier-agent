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

	public String setUpDirectory(String cellIdentifier);
	
	public boolean createIniFile(String directoryPath);
	
	public boolean getAllStubs(String serverAddress, String extractTo);

	public boolean createTempFile(String filePath);
}
