/**
 * 
 */
package com.vizier.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * @author aniruddha
 *
 */
public interface VizierBackendClient {

	public boolean fetchCellContentTo(String cellIdentifier, String filePath) throws URISyntaxException;
	
	public boolean syncCell(String cellIdentifier, String filePath);
}
