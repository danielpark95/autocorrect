# Autocorrect
Java implementation of Autocorrect. 

## Usage
### Command Line
Once inside the cloned directory, `cd` into `src` folder and type the following commands to run the client application.
```
javac main/AutocorrectApp.java
java main/AutocorrectApp
```

<a href="url"><img src="https://github.com/danielpark95/autocorrect/blob/master/img/command_line_demo.png" width="700" ></a>


### IDE
Import the project into Eclipse/IntelliJ/etc. and run AutocorrectApp.java.

<a href="url"><img src="https://github.com/danielpark95/autocorrect/blob/master/img/eclipse_demo.png" width="1000" ></a>

## Implementation
### BasicAutocorrect
Basic implementation of Autocorrect that returns a suggestion for a misspelled word.

Given a String s, makeGuess(s) checks if a valid word can be formed by
1) adding one letter
2) removing one letter
3) replacing one letter
4) switching two adjacent letters
 
In case of multiple suggestions, the answer with highest count will be returned.

BasicAutocorrect can only return a suggestion that's one character away from input.

### AdvancedAutocorrect
Advanced implementation of Autocorrect that
1) favors suggestions that are common words over uncommon words
2) favors suggestions with length similar to input
3) finds suggestions up to 3 letters different from input
