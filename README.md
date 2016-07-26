# simpledb
java application to simulate as in-memory db


HOW TO RUN
----------

1. navigate to the directory "turo_simpledb_noIDE" in commandline
2. Compile
      javac src/com/hogwart/simpledb/*.java
      
      
3. Run
      //To read commands from STDIN
      java -cp ./src com.hogwart.simpledb.Main
      
      //To read commands from a file
      java -cp ./src com.hogwart.simpledb.Main <path_to_file>
      E.g. java -cp ./src com.hogwart.simpledb.Main input5.txt
