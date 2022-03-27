/**
 * 
 */
package com.vizier.client;

/**
 * @author aniruddha
 *
 */
public interface VizierBackendClient {

	public boolean fetchCellContentTo(String cellIdentifier, String filePath);
	
	public boolean syncCell();
}
