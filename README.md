In build.xml:

Set up [jdk.version];
Set up [ANT_HOME] which is a path to apache-ant builder;
Set up [TOMCAT_HOME] which is a path to Tomcat folder;
Set up [tomcat.port] which would be a port for Tomcat;

Ant war - creates a war archive in [./ant_build folder];
Ant (default) deploys application on Tomcat;

In [./src/main/resources/config.properties] :
Change SYSTEM in line [jdbc.username=SYSTEM] to your username in your oracle database;
Change root in line [jdbc.password=root] to your password in your oracle database;