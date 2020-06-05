#!/bin/bash
src="FOBT/src"
rm -f FOBT.jar
cd ..
jar cf $src/FOBT.jar $src/*.java FOBT/LICENSE FOBT/VERSION FOBT/README.md
cd $src
javac *.java
jar ufe FOBT.jar Main *.class
cd ..
rm src/*.class
mv src/FOBT.jar FOBT.jar
chmod 705 FOBT.jar
