HOW TO RUN
————————————
1. navigate to the Directory “turo_simpledb_noIDE” in commandline
2. Compile
	javac src/com/hogwart/simpledb/*.java

3. Run
	//To read commands from STDIN
	java -cp ./src com.hogwart.simpledb.Main

	//To read commands from File
	java -cp ./src com.hogwart.simpledb.Main <path_to_file>
	E.g. java -cp ./src com.hogwart.simpledb.Main input5.txt



My Implementation Logic
-----------------------

The singleton class SimpleDb is the core class to handle all the logic of the db.
    - rootTxn and
    - counterMap

are the chief components of the class.

rootTxn acts as the main storage system of the in-memory database. For no transaction started,
all variables are stored in this root transaction.

counterMap keeps the record of counts of variables for given value.

Initially rootTxn is also the current transaction. When new transaction begins,
new instance of Transaction class is created as child of current transaction. Then
the new transaction becomes the new current transaction. New Set and Unset
commands are saved in the current transaction. CounterMap is updated accordingly.

Upon encounter of Rollback command, all commands in current transaction are
ignored but counterMap is updated based on the commands stored in current
transaction. currentTransaction pointer is also updated accordingly.

Similarly, upon encounter with Commit command, all the commands in all
transactions starting from child of rootTxn are reflected into rootTxn
storage. In this case, counterMap is not necessary to update as it is
updated with each inidividual command. Afte commit, rootTxn becomes the
current transaction.