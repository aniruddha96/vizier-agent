/**
 * 
 */
package com.vizier.state;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.vizier.constants.VizierAgentConstants.stateInfoFile;

/**
 * @author aniruddha
 * @author ksphoorthi
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
	public static void init(String cellIdentifier, String file) {
		File f =new File(file);

		try {
			if(f.createNewFile()) {
				state = new State();
				state.setWatcherActive(true);
				state.setCellIdentifier(cellIdentifier);
				flush(f.getPath());
			}else {
				state = objectMapper.readValue(new File(file), State.class);
				state.setWatcherActive(true);
				state.setCellIdentifier(cellIdentifier);
				flush(file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void flush(String file) {
		try {
			objectMapper.writeValue(new File(file), state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
