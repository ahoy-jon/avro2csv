Avro2Csv 
==

Build
-----

To build the project, you can use the embded SBT :

```
./sbt assembly
```

It generates a self contained jar in 

```
./avro2csv-full.jar
```

Run
----- 

The tool takes two arguments, the input file, then the output file.

Examples : 

```
#defined input output
java -jar ./avro2csv-full.jar syncInMeta.avro syncInMeta.csv

#input from stdin
cat syncInMeta.avro | java -jar ./avro2csv-full.jar - syncInMeta.csv

#output to stdout
java -jar ./avro2csv-full.jar syncInMeta.avro -

#input from stdin, output to stdout
java -jar ./avro2csv-full.jar - -

``` 

Bugs
---- 

If you find one, tell us !

