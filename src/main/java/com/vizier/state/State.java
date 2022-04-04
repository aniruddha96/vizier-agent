/**
 * 
 */
package com.vizier.state;

import java.io.Serializable;

/**
 * @author aniruddha
 *
 */
public class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean watcherActive;

	public boolean isWatcherActive() {
		return watcherActive;
	}

	public void setWatcherActive(boolean watcherActive) {
		this.watcherActive = watcherActive;
	}
	
	
	
}
