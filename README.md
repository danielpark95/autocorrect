# Autocorrect
Java implementation of Autocorrect. 

## Usage
### Command Line
Once inside the cloned directory, `cd` into `src` folder and type the following commands to run the client application.
```
javac main/AutocorrectApp.java
java main/AutocorrectApp
```
### IDE
Import the project into Eclipse/IntelliJ/etc. and run AutocorrectApp.java.

### Example
[[https://github.com/danielpark95/autocorrect/blob/master/img/example.png|alt=example]]


## Implementation
### BasicAutocorrect
Basic implementation of Autocorrect that returns the most frequently suggested word after running 4 methods (add one letter, remove one letter, replace one letter, switch two adjacent letters).


### AdvancedAutocorrect
Advanced implementation of AutoCorrect that
1) extends BasicAutocorrect
2) takes into account word frequency in final ranking
3) weighs equal-length words returned from replaceOneLetter() and switchTwoLetters() more than addOneLetter() and removeOneLetter() (e.g. "esting" could be "eating" or "testing", but "eating" will be prioritized) 