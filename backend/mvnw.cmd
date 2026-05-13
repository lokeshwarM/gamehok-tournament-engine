@echo off
setlocal
set JAVA_EXECUTABLE=java
if defined JAVA_HOME set JAVA_EXECUTABLE=%JAVA_HOME%\bin\java
%JAVA_EXECUTABLE% -classpath ".mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=%~dp0" org.apache.maven.wrapper.MavenWrapperMain %*
