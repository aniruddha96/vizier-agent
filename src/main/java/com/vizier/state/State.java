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

	String cellIdentifier;

	public boolean isWatcherActive() {
		return watcherActive;
	}

	public String getCellIdentifier(){
		return cellIdentifier;
	}

	public void setWatcherActive(boolean watcherActive) {
		this.watcherActive = watcherActive;
	}

	public void setCellIdentifier(String cellIdentifier){
		this.cellIdentifier = cellIdentifier;
	}
	
}
