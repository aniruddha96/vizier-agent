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
	
	
	public static void refresh() {
		try {
			state = objectMapper.readValue(new File("D:\\github\\vizier-agent\\cell_content\\state.json"), State.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void init(String file) {
		File f =new File("D:\\github\\vizier-agent\\cell_content\\state.json");

		try {
			if(f.createNewFile()) {
				state = new State();
				state.setWatcherActive(false);
				flush();
			}else {
				state = objectMapper.readValue(new File("D:\\github\\vizier-agent\\cell_content\\state.json"), State.class);
				state.setWatcherActive(false);
				flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void flush() {
		try {
			objectMapper.writeValue(new File("D:\\github\\vizier-agent\\cell_content\\state.json"), state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
