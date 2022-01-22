# MSX Font Editor - Version 1.7.0

## 0- What's new?

1.7.0 - Refactor to use Apache Maven and new package structure

1.6.1 - Bug fixed for not recognizing right button while dragging mouse on character panel in some Java's.

1.6 - New tool for searching fonts inside files, ASCII codes reference, bug fixes.

1.5 - Now deals with screen 1 colors, load/save in many formats, added mouse tutorial.

1.4 - Character shift tools, double click to copy character to editor and back.

1.3 - Font multiselection allowed, mouse drag allowed to select multiple characters, possible to copy/paste multiple charcters between fonts.

1.2 - GUI improvements

1.1 - Some bug fixes


##1- Compiling and running MSX Font Editor

Once MSX Font Editor was coded in Java, it is compatible with many O.S. such Windows, Linux and Mac OS.
It is not necessary to recompile the Java executables to run in a specific operating system. The Java binaries are the same for any O.S.

###1.1 Requirements
* At least JDK 11 installed:
https://jdk.java.net/java-se-ri/11
* At least Apache Maven 3.6.4
https://maven.apache.org/download.cgi

###1.1. Compiling
* From the project's root directory type on a command line:

```mvn clean package```

Maven will create a `.jar` file in the `target` directory.

### 1.3. Running the `.jar` file
Type on the command line:
* Linux

```java -jar target/mfe-1.7.0.jar```

* Windows
```
java -jar target\mfe-1.7.0.jar
```


##2- MSX Font Editor Homepage and contact:

###Version 1.7.0:

Author: Emiliano Vaz Fraga

E-mail: efraga@gmail.com

###Up to version 1.6.1:
Author: Marcelo Teixeira Silveira

Homepage: marmsx.msxall.com

E-mail: flamar98@hotmail.com
