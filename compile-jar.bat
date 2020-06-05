@ECHO OFF
SET "ReadMe=FOBT\README.md"
ATTRIB -r FOBT.jar
DEL FOBT.jar
CD ..
jar cf FOBT\src\FOBT.jar FOBT\src\*.java FOBT\LICENSE FOBT\VERSION %ReadMe%
CD FOBT\src
javac *.java
jar ufe FOBT.jar Main *.class
CD ..
DEL src\*.class
MOVE src\FOBT.jar FOBT.jar
ATTRIB +r FOBT.jar
