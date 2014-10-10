cd .. && export CLASSPATH=`pwd`
if cd ./FillWord5 && javac BuildFillWord.java && java FillWord5.BuildFillWord javac *.java
then
cd .. && jar cfm FillWord5/FillWord5.jar FillWord5/manifest.txt FillWord5
fi
