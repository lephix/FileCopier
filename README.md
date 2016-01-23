# FileCopier

A File Copier that can copy file tree to the destination with a simple regular expression.

Status: ![status](https://api.travis-ci.org/lephix/FileCopier.svg)

## Usage

### With source code

+ Download source code from git.
+ Use maven to package. `mvn clean install`
+ Copy the jar file from target folder to wherever you want.
+ Create a application.proerties alone with the jar file. The content likes following.
```
    # available commands:
    # stm: copy single file/folder to multiple places matches the pattern
    # mts: copy file/folder matches the pattern from multiple places to single folder
    command=stm

    source=/path/of/source/folder # The source file/folder
    target=/path/of/target/folder # The target file/folder
    file.pattern=/regex/pattern # The pattern need to be matched
```
+ Run the copier. `java -jar FileCopier-<version>.jar`


