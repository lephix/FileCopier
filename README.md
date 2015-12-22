# FileCopier

A File Copier that can copy file tree to the destination with a simple regular expression.

## Usage

### With source code

+ Download source code from git.
+ Use maven to package. `mvn clean install`
+ Copy the jar file from target folder to wherever you want.
+ Create a application.proerties alone with the jar file. The content likes following.
```
    source.folder=/path/of/source/folder # The source folder
    target.folder=/path/of/target/folder # The target folder
    file.pattern=/regex/pattern # The pattern need to be matched
```
+ Run the copier. `java -jar FileCopier-<version>.jar`


