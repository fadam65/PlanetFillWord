cd ..
set CLASSPATH=%cd%
cd FillWord5
javac BuildFillWord.java
java FillWord5.BuildFillWord javac *.java
cd ..
jar cfm FillWord5/FillWord5.jar FillWord5/manifest.txt FillWord5
cd FillWord5
