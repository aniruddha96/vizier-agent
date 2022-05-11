# vizier-agent
This repository contains the source code for local agent that works with [VizierDB](https://github.com/VizierDB/vizier-scala)
to open cells in local editor. This project aims to achieve the Open In Editor functionality for vizier notebook cells as mentioned in 
[Issue28](https://github.com/VizierDB/vizier-scala/issues/28).

It has code components that enable flow of data to and from Vizier db when the local editor session is active.

The code contains the following packages:

1. package com.vizier.agent.editor<br>
This package contains implementation for launching editors for each operating system : Windows, Linux and MacOS. 

2. package com.vizier.agent.filewatcher<br>
This package contains file watcher implementation that monitors changes to local python and state file.

3. package com.vizier.client<br>
This package contains method that interact with vizier db api to get/ post code contents from/to vizier db.

4. package com.vizier.constants<br>
This package contains application constants like cell directory paths, mypy configuration file path etc.

5. package com.vizier.directory<br>
This package contains methods that sets up a local directory structure for every vizier cell opened in editor.

6. package com.vizier.state<br>
This package contains the implementation of State File that manages the file watcher lifecycle.

7. package com.vizier.stub.client<br>
This package contains implementations that invoke stub server API to fetch the stubs for installed python packages.



Setup Prerequisites
1.	Install Java JDK 8 and set the JAVA_HOME path to the JDK installation path.
2.	Install mypy using the pip tool
    python3 -m pip install mypy

The installers for different operating systems a available in the following folders at project root:
To install the agent, perform the following steps based on the underlying operating system
Clone the vizier-agent source code by issuing the following command:<br>
```
git clone https://github.com/aniruddha96/vizier-agent.git
```


Windows Installation
1. Open command prompt
2. cd ${PROJECT_PATH}/windows_installer
3. install.bat

Linux Installation
1. Open terminal
2. cd ${PROJECT_PATH}
3. sudo ubuntu_agent_installer.sh

Mac Installation
1. Open terminal
2. cd ${PROJECT_PATH}/mac_installer
3. chmod +x install.sh
4. ./install.sh
