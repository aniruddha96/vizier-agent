/**
 * 
 */
package com.vizier.state;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aniruddha
 *
 */
public class StateHandler {

	static ObjectMapper objectMapper = new ObjectMapper();
	static State state;
	
	public static State getState() {
		return state;
	}
	
	
	public static void refresh(String file) {
		try {
			state = objectMapper.readValue(new File(file), State.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void init(String file) {
		File f =new File(file);

		try {
			if(f.createNewFile()) {
				state = new State();
				state.setWatcherActive(false);
				state.setCellIdentifier(file);
				flush(f.getPath());
			}else {
				state = objectMapper.readValue(new File(file), State.class);
				state.setWatcherActive(false);
				state.setCellIdentifier(file);
				flush(file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void flush(String file) {
		try {
			objectMapper.writeValue(new File(file), state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
