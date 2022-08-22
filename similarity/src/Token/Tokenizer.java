package Token;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tokenizer
{

	// moves the scanner forward n characters. Also changes the delimiter to count everything, including spaces.
	// Method inputs are a scanner, and an integer for the number of times to move forward
	public static void RepeatNext(Scanner s, int n) {

		// \\b means a word boundary. \\B means a non-word boundary. Together it will stop on every character
		s.useDelimiter("(\\b|\\B)");

		// repeats the next command n times to move forward n characters.
		for (int i=1; i <= n; i++) {
			s.next();		
		}

	}

	// Find and replace code for a file.
	// inputs are a file name, the string to find, the string to replace.
	public static void FindReplace(String fileName, String find, String replace) throws IOException {

		File file = new File(fileName); //creates new file from the file name
		String unchangedLine = ""; // a string that will store all the text of the file
		BufferedReader reader = new BufferedReader(new FileReader(file)); // creats a new buffared reader
		String line = reader.readLine(); // reads the first line of the file
		while (line != null) // while we haven't reached the end of this line
		{
			unchangedLine = unchangedLine + line + System.lineSeparator(); //adds the line to the string
			line = reader.readLine(); // reads the next line. Each line is properly seperated. 
		}
		String changedLine = unchangedLine.replaceAll(find, replace); // uses the replaceAll command to properly find and replace
		FileWriter writer = new FileWriter(file); //opens the file writer
		writer.write(changedLine); // write the string with the changed text to the file
		reader.close(); // close reader
		writer.close(); // close writer

	}

	// method to find array positions to replace with a token
	public static Boolean CheckArray(Scanner s) {
		s.useDelimiter("[\\s\\(\\{\\)\\}]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } 
		// after finding something, it will move forward the correct number of characters to get past what if found, then return true
		if (s.hasNext( "\\[\\].*" )) { RepeatNext(s,2); return true;} // finds square brackets with nothing in between
		if (s.hasNext( "\\[\\d\\].*" )) { RepeatNext(s,3); return true;} // finds square brackets with a digit in between
		if (s.hasNext( "\\[\\d\\d\\].*" )) { RepeatNext(s,4);return true;} // finds square brackets with two digits in between
		if (s.hasNext( "\\[\\d\\d\\d\\].*" )) { RepeatNext(s,5);return true;}
		if (s.hasNext( "\\[\\d\\d\\d\\d\\].*" )) { RepeatNext(s,6);return true;}
		if (s.hasNext( "\\[\\d\\d\\d\\d\\d\\].*" )) { RepeatNext(s,7);return true;} // finds square brackets with 5 digits in between.
		// arrays with more than 100 000 positions will not be detected, but those should be rare. 
		s.useDelimiter("(\\b|\\B)"); // will move forward one character at a time, counting spaces.
		return false; // returns false if no array detected

	}

	// List of bracket characters that will get replaced by the bracket token if detected. 	       
	public static Boolean CheckBracket(Scanner s) {
		// after finding something, it will move forward the correct number of characters to get past what if found, then return true
		if (s.hasNext( "[}]" )) { RepeatNext(s,1); return true;} // find }
		if (s.hasNext( "[]]" ))  { RepeatNext(s,1); return true;} // finds ]
		if (s.hasNext( "[)]" ))  { RepeatNext(s,1); return true;} // finds )
		if (s.hasNext( "[{]" ))  { RepeatNext(s,1); return true;} // finds {
		if (s.hasNext( "[(]" ))  { RepeatNext(s,1); return true;} // finds (
		if (s.hasNext( "\\[" ))  { RepeatNext(s,1); return true;} // finds [

		return false; // returns false if no bracket detected
	}
	
	// List of the comparator characters that will get replaced by the comparator token if detected
	public static Boolean CheckComparators(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 
		if (s.hasNext( "==.*")) { RepeatNext(s,2); return true;} // detects ==
		if (s.hasNext( "!=.*"))  { RepeatNext(s,2); return true;} // detects !=
		if (s.hasNext( ">=.*"))  { RepeatNext(s,2); return true;} // detects >=
		if (s.hasNext( "<=.*"))  { RepeatNext(s,2); return true;} // detects <=     
		if (s.hasNext( ">.*" ))  { RepeatNext(s,1); return true;} // detects >
		if (s.hasNext( "<.*" ))  { RepeatNext(s,1); return true;} // detects <
		// if detected, it moves the scanner forward the appropriate number of characters to get past what was found, and returns true

		s.useDelimiter("(\\b|\\B)"); // will move forward one character at a time, counting spaces.
		return false; // returns false if nothing found

	}

	// List of math characters that will get replaced by the math token if detected.
	public static Boolean CheckMath(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 
		if (s.hasNext( "\\+\\+.*")) { RepeatNext(s,2); return true;} // detects ++
		if (s.hasNext( "\\-\\-.*"))  { RepeatNext(s,2); return true;} // detects --
		if (s.hasNext( "\\=.*"))  { RepeatNext(s,1);  return true;} // detects =
		if (s.hasNext( "\\/.*"))  { RepeatNext(s,1);  return true;} // detects /     
		if (s.hasNext( "\\*.*" ))  { RepeatNext(s,1); return true;} // detects *
		if (s.hasNext( "\\-.*" ))  { RepeatNext(s,1); return true;} // detects -
		if (s.hasNext( "\\+.*" ))  { RepeatNext(s,1); return true;} // detects +
		if (s.hasNext( "\\%.*" ))  { RepeatNext(s,1); return true;} // detects %


		s.useDelimiter("(\\b|\\B)");  // will move forward one character at a time, counting spaces.
		return false; // returns false if nothing found

	}

	// List of logic characters that will get replaced by the logic token if detected.
	public static Boolean CheckLogic(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 
		if (s.hasNext( "\\|\\|.*")) { s.useDelimiter(""); s.next(); s.next(); return true;} // detects ||
		if (s.hasNext( "\\&\\&.*"))  { s.useDelimiter(""); s.next(); s.next(); return true;} // detects &&
		if (s.hasNext( "\\?.*"))  { s.useDelimiter(""); s.next();  return true;} // detects ?
		if (s.hasNext( "\\!.*"))  { s.useDelimiter(""); s.next();  return true;} // detects !      

		s.useDelimiter("(\\b|\\B)");  // will move forward one character at a time, counting spaces.
		return false;  // returns false if nothing found

	}

	// List of Bitwise symbols that will get replaced by the bitwise token if detected.
	public static Boolean CheckBitWise(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		if (s.hasNext("\\&.*")){ RepeatNext(s,"&".length());   return true;} // detects &   
		if (s.hasNext("\\|.*" )){ RepeatNext(s,"|".length());   return true;}  // detects | 
		if (s.hasNext("\\^.*" )){ RepeatNext(s,"^".length());   return true;}  // detects ^ 
		if (s.hasNext("\\~.*" )){ RepeatNext(s,"~".length());   return true;}  // detects ~ 
		if (s.hasNext("\\<<.*" )){ RepeatNext(s,"<<".length());   return true;}  // detects << 
		if (s.hasNext("\\>>.*" )){ RepeatNext(s,">>".length());   return true;}  // detects >> 
		if (s.hasNext("\\>>>.*" )){ RepeatNext(s,">>>".length());   return true;}  // detects >>> 

		s.useDelimiter("(\\b|\\B)");  // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}

	// List of Boolean characters that will get replaced by the Boolean token if detected.
	public static Boolean CheckBoolean(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		if (s.hasNext("true\\W*")){ RepeatNext(s,"true".length());   return true;} // detects true   
		if (s.hasNext("false\\W*" )){ RepeatNext(s,"false".length());   return true;}  // detects false 

		s.useDelimiter("(\\b|\\B)");  // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}
	
	// List of variable type names that will get replaced with the vartype token if detected.
	public static Boolean CheckVarType(int lang,Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		// detects the word, moves forward the correct number of characters to get past the word, and returns true 
		// "\\W*" means any number of non-word character can appear after the word, and it will still get detected.
		if (lang == 1) { // if the language is java look for these words
			if (s.hasNext( "boolean\\W*" )){ RepeatNext(s,"boolean".length());   return true;}      
			if (s.hasNext( "String\\W*" )){ RepeatNext(s,"String".length());   return true;}  
			if (s.hasNext( "int\\W*" )){ RepeatNext(s,"int".length());   return true;}   
			if (s.hasNext( "char\\W*")){ RepeatNext(s,"char".length());   return true;}   
			if (s.hasNext( "float\\W*" )){ RepeatNext(s,"float".length());   return true;}  
			if (s.hasNext( "long\\W*" )){ RepeatNext(s,"long".length());   return true;}
			if (s.hasNext( "void\\W*")){ RepeatNext(s,"void".length());   return true;}   
			if (s.hasNext( "double\\W*" )){ RepeatNext(s,"double".length());   return true;}
			if (s.hasNext( "short\\W*" )){ RepeatNext(s,"short".length());   return true;}
			if (s.hasNext( "Integer\\W*" )){ RepeatNext(s,"Integer".length());   return true;}  

			if (s.hasNext( "<String>\\W*" )){ RepeatNext(s,"<String>".length());   return true;} 
			if (s.hasNext( "<Integer>\\W*" )){ RepeatNext(s,"<Integer>".length());   return true;}
			if (s.hasNext( "<Boolean>\\W*" )){ RepeatNext(s,"<Boolean>".length());   return true;} 
			if (s.hasNext( "<Character>\\W*" )){ RepeatNext(s,"<Character>".length());   return true;} 
			if (s.hasNext( "<Float>\\W*" )){ RepeatNext(s,"<Float>".length());   return true;} 
			if (s.hasNext( "<Long>\\W*" )){ RepeatNext(s,"<Long>".length());   return true;} 
		}
		else if (lang == 2) { // if the language is c++, look for these words
			if (s.hasNext( "char\\W*" )){ RepeatNext(s,"char".length());   return true;}  
			if (s.hasNext( "char16_t\\W*" )){ RepeatNext(s,"char16_t".length());   return true;}    
			if (s.hasNext( "char32_t\\W*" )){ RepeatNext(s,"char32_t".length());   return true;}    
			if (s.hasNext( "wchar_t\\W*" )){ RepeatNext(s,"wchar_t".length());   return true;}    
			if (s.hasNext( "signed\\W*" )){ RepeatNext(s,"signed".length());   return true;}    
			if (s.hasNext( "short\\W*" )){ RepeatNext(s,"short".length());   return true;}    
			if (s.hasNext( "int\\W*" )){ RepeatNext(s,"int".length());   return true;}    
			if (s.hasNext( "long\\W*" )){ RepeatNext(s,"long".length());   return true;}    
			if (s.hasNext( "char\\W*" )){ RepeatNext(s,"char".length());   return true;}    
			if (s.hasNext( "unsigned\\W*" )){ RepeatNext(s,"unsigned".length());   return true;}   
			if (s.hasNext( "float\\W*" )){ RepeatNext(s,"float".length());   return true;}    
			if (s.hasNext( "double\\W*" )){ RepeatNext(s,"double".length());   return true;}    
			if (s.hasNext( "bool\\W*" )){ RepeatNext(s,"bool".length());   return true;}    
			if (s.hasNext( "void\\W*" )){ RepeatNext(s,"void".length());   return true;}    
			if (s.hasNext( "decltype\\W*" )){ RepeatNext(s,"decltype".length());   return true;}    
			if (s.hasNext( "nullptr\\W*" )){ RepeatNext(s,"nullptr".length());   return true;}    


		}
		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}

	// List of loop names that will get replaced with the loop token if detected.
	public static Boolean CheckLoops(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 
		
		// detects the word, moves forward the correct number of characters to get past the word, and returns true 
		// "\\W*" means any number of non-word character can appear after the word, and it will still get detected.
		if (s.hasNext( "for\\W*" )){ RepeatNext(s,"for".length());   return true;}  
		if (s.hasNext( "while\\W*" )){ RepeatNext(s,"while".length());   return true;}
		if (s.hasNext( "do\\W*"  )){ RepeatNext(s,"do".length());   return true;}  

		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}

	// List of if words that will get replaced with the if token if detected.
	public static Boolean CheckIfs(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		// detects the word, moves forward the correct number of characters to get past the word, and returns true 
		// "\\W*" means any number of non-word character can appear after the word, and it will still get detected.
		if (s.hasNext( "if\\W*" )){ RepeatNext(s,"if".length());   return true;}   
		if (s.hasNext( "else\\W*"  )){ RepeatNext(s,"else".length());   return true;}  

		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}
	
	// List of if words that will get replaced with the output token if detected.
	public static Boolean CheckOutput(int lang, Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		// detects the word, moves forward the correct number of characters to get past the word, and returns true 
		// "\\W*" means any number of non-word character can appear after the word, and it will still get detected.
		if (lang == 1 || lang ==2) { // if the language is java look for these words
			if (s.hasNext( "print\\W*" )){ RepeatNext(s,"print".length());   return true;}   
			if (s.hasNext( "println\\W*"  )){ RepeatNext(s,"println".length());   return true;}  
			if (s.hasNext( "printf\\W*"  )){ RepeatNext(s,"printf".length());   return true;}  
		}
		else if (lang == 2) { // if the language is c++, look for these words 
			if (s.hasNext( "cout\\W*" )){ RepeatNext(s,"cout".length());   return true;}   
			if (s.hasNext( "cerr\\W*" )){ RepeatNext(s,"cerr".length());   return true;}   
			if (s.hasNext( "clog\\W*" )){ RepeatNext(s,"clog".length());   return true;}   
		}

		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false;// returns false if nothing found

	}

	// Checks if there is the word class to replace with the class token
	public static Boolean CheckClass(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		if (s.hasNext("class\\W*" )){ RepeatNext(s,"class".length());   return true;}  

		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false; // returns false if nothing found

	}

	// Checks if there is the word return to replace with the return token
	public static Boolean CheckReturn(Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+"); // change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 

		if (s.hasNext( "return\\W*" )){ RepeatNext(s,"return".length());   return true;}   

		s.useDelimiter("(\\b|\\B)"); // revert scanner to move forward one character at a time, counting spaces.
		return false; // returns false if nothing found

	}


	// List of java keywords that will get replaced by the keyword token if detected.
	public static Boolean CheckKeywords(int lang, Scanner s) {

		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+");// change the delimiter to stop at a whitespace character (\\s), or brackets ( ) { } [ ] 
		// detects the word, moves forward the correct number of characters to get past the word, and returns true 
		// "\\W*" means any number of non-word character can appear after the word, and it will still get detected.
		if (lang == 1) { // if the language is java look for these words
			if (s.hasNext("abstract\\W*")){ RepeatNext(s,"abstract".length());   return true;}    
			if (s.hasNext("assert\\W*" )){ RepeatNext(s,"assert".length());   return true;}   
			if (s.hasNext("break\\W*" )){ RepeatNext(s,"break".length());   return true;}   
			if (s.hasNext( "byte\\W*")){ RepeatNext(s,"byte".length());   return true;}   
			if (s.hasNext( "case\\W*")){ RepeatNext(s,"case".length());   return true;}   
			if (s.hasNext("catch\\W*" )){ RepeatNext(s,"catch".length());   return true;}   
			if (s.hasNext( "const\\W*")){ RepeatNext(s,"const".length());   return true;}   
			if (s.hasNext( "continue\\W*")){ RepeatNext(s,"continue".length());   return true;}   
			if (s.hasNext( "default\\W*" )){ RepeatNext(s,"default".length());   return true;}   
			if (s.hasNext( "enum\\W*" )){ RepeatNext(s,"enum".length());   return true;}   
			if (s.hasNext( "extends\\W*" )){ RepeatNext(s,"extends".length());   return true;}   
			if (s.hasNext( "final\\W*" )){ RepeatNext(s,"final".length());   return true;}   
			if (s.hasNext( "finally\\W*" )){ RepeatNext(s,"finally".length());   return true;}   
			if (s.hasNext( "goto\\W*" )){ RepeatNext(s,"goto".length());   return true;}   
			if (s.hasNext( "implements\\W*" )){ RepeatNext(s,"implements".length());   return true;}   
			if (s.hasNext( "import\\W*" )){ RepeatNext(s,"import".length());   return true;}   
			if (s.hasNext( "instanceof\\W*")){ RepeatNext(s,"instanceof".length());   return true;}   
			if (s.hasNext( "interface\\W*" )){ RepeatNext(s,"interface".length());   return true;}   
			if (s.hasNext( "native\\W*" )){ RepeatNext(s,"native".length());   return true;}   
			if (s.hasNext( "new\\W*")){ RepeatNext(s,"new".length());   return true;}   
			if (s.hasNext("null\\W*" )){ RepeatNext(s,"null".length());   return true;} // detects null
			if (s.hasNext( "package\\W*")){ RepeatNext(s,"package".length());   return true;}   
			if (s.hasNext( "private\\W*" )){ RepeatNext(s,"private".length());   return true;}   
			if (s.hasNext( "protected\\W*")){ RepeatNext(s,"protected".length());   return true;}   
			if (s.hasNext( "public\\W*")){ RepeatNext(s,"public".length());   return true;}   
			if (s.hasNext( "static\\W*")){ RepeatNext(s,"static".length());   return true;}   
			if (s.hasNext( "strictfp\\W*")){ RepeatNext(s,"strictfp".length());   return true;}   
			if (s.hasNext( "super\\W*" )){ RepeatNext(s,"super".length());   return true;}   
			if (s.hasNext( "switch\\W*")){ RepeatNext(s,"switch".length());   return true;}   
			if (s.hasNext( "synchronized\\W*")){ RepeatNext(s,"synchronized".length());   return true;}   
			if (s.hasNext( "this\\W*" )){ RepeatNext(s,"this".length());   return true;}   
			if (s.hasNext( "throw\\W*" )){ RepeatNext(s,"throw".length());   return true;}   
			if (s.hasNext( "throws\\W*")){ RepeatNext(s,"throws".length());   return true;}    
			if (s.hasNext( "transient\\W*")){ RepeatNext(s,"transient".length());   return true;}   
			if (s.hasNext( "try\\W*")){ RepeatNext(s,"try".length());   return true;}   
			if (s.hasNext( "volatile\\W*")){ RepeatNext(s,"volatile".length());   return true;}   
		}
		else if (lang == 2) { // if the language is c++, look for these words
			if (s.hasNext("and\\W*")){ RepeatNext(s,"and".length());   return true;}    
			if (s.hasNext("and_eq\\W*" )){ RepeatNext(s,"and_eq".length());   return true;}   
			if (s.hasNext("asm\\W*" )){ RepeatNext(s,"asm".length());   return true;}   
			if (s.hasNext( "auto\\W*")){ RepeatNext(s,"auto".length());   return true;}   
			if (s.hasNext( "bitand\\W*")){ RepeatNext(s,"bitand".length());   return true;}   
			if (s.hasNext("bitor\\W*" )){ RepeatNext(s,"bitor".length());   return true;}   
			if (s.hasNext( "break\\W*")){ RepeatNext(s,"break".length());   return true;}   
			if (s.hasNext( "case\\W*" )){ RepeatNext(s,"case".length());   return true;}   
			if (s.hasNext( "catch\\W*" )){ RepeatNext(s,"catch".length());   return true;}   
			if (s.hasNext( "compl\\W*" )){ RepeatNext(s,"compl".length());   return true;}   
			if (s.hasNext( "const\\W*" )){ RepeatNext(s,"const".length());   return true;}   
			if (s.hasNext( "const_cast\\W*" )){ RepeatNext(s,"const_cast".length());   return true;}   
			if (s.hasNext( "continue\\W*" )){ RepeatNext(s,"continue".length());   return true;}   
			if (s.hasNext( "default\\W*" )){ RepeatNext(s,"default".length());   return true;}   
			if (s.hasNext( "delete\\W*" )){ RepeatNext(s,"delete".length());   return true;}   
			if (s.hasNext( "do\\W*")){ RepeatNext(s,"do".length());   return true;}   
			if (s.hasNext( "dynamic_cast\\W*" )){ RepeatNext(s,"dynamic_cast".length());   return true;}   
			if (s.hasNext( "enum\\W*" )){ RepeatNext(s,"enum".length());   return true;}   
			if (s.hasNext( "explicit\\W*")){ RepeatNext(s,"explicit".length());   return true;}   
			if (s.hasNext( "extern\\W*")){ RepeatNext(s,"extern".length());   return true;}   
			if (s.hasNext( "friend\\W*" )){ RepeatNext(s,"friend".length());   return true;}   
			if (s.hasNext( "goto\\W*")){ RepeatNext(s,"goto".length());   return true;}   
			if (s.hasNext( "inline\\W*")){ RepeatNext(s,"inline".length());   return true;}   
			if (s.hasNext( "mutable\\W*")){ RepeatNext(s,"mutable".length());   return true;}   
			if (s.hasNext( "namespace\\W*")){ RepeatNext(s,"namespace".length());   return true;}   
			if (s.hasNext( "not\\W*" )){ RepeatNext(s,"not".length());   return true;}   
			if (s.hasNext( "not_eq\\W*")){ RepeatNext(s,"not_eq".length());   return true;}   
			if (s.hasNext( "operator\\W*")){ RepeatNext(s,"operator".length());   return true;}   
			if (s.hasNext( "or_eq\\W*" )){ RepeatNext(s,"or_eq".length());   return true;}   
			if (s.hasNext( "private\\W*" )){ RepeatNext(s,"private".length());   return true;}   
			if (s.hasNext( "protected\\W*")){ RepeatNext(s,"protected".length());   return true;}    
			if (s.hasNext( "public\\W*")){ RepeatNext(s,"public".length());   return true;}   
			if (s.hasNext( "register\\W*")){ RepeatNext(s,"register".length());   return true;}   
			if (s.hasNext( "reinterpret_cast\\W*")){ RepeatNext(s,"reinterpret_cast".length());   return true;}   
			if (s.hasNext( "signed\\W*")){ RepeatNext(s,"signed".length());   return true;}   
			if (s.hasNext( "sizeof\\W*")){ RepeatNext(s,"sizeof".length());   return true;}   
			if (s.hasNext( "static\\W*")){ RepeatNext(s,"static".length());   return true;}   
			if (s.hasNext( "static_cast\\W*")){ RepeatNext(s,"static_cast".length());   return true;}   
			if (s.hasNext( "struct\\W*")){ RepeatNext(s,"struct".length());   return true;}   
			if (s.hasNext( "switch\\W*")){ RepeatNext(s,"switch".length());   return true;}   
			if (s.hasNext( "template\\W*")){ RepeatNext(s,"template".length());   return true;}   
			if (s.hasNext( "throw\\W*")){ RepeatNext(s,"throw".length());   return true;}   
			if (s.hasNext( "try\\W*")){ RepeatNext(s,"try".length());   return true;}   
			if (s.hasNext( "typedef\\W*")){ RepeatNext(s,"typedef".length());   return true;}   
			if (s.hasNext( "typeid\\W*")){ RepeatNext(s,"typeid".length());   return true;}   
			if (s.hasNext( "typename\\W*")){ RepeatNext(s,"typename".length());   return true;}   
			if (s.hasNext( "union\\W*")){ RepeatNext(s,"union".length());   return true;}   
			if (s.hasNext( "unsigned\\W*")){ RepeatNext(s,"unsigned".length());   return true;}   
			if (s.hasNext( "using\\W*")){ RepeatNext(s,"using".length());   return true;}   
			if (s.hasNext( "virtual\\W*")){ RepeatNext(s,"virtual".length());   return true;}   
			if (s.hasNext( "volatile\\W*")){ RepeatNext(s,"volatile".length());   return true;}   
			if (s.hasNext( "this\\W*")){ RepeatNext(s,"this".length());   return true;}   
			if (s.hasNext( "xor\\W*")){ RepeatNext(s,"xor".length());   return true;}   
			if (s.hasNext( "xor_eq\\W*")){ RepeatNext(s,"xor_eq".length());   return true;}   
		}

		s.useDelimiter("(\\b|\\B)");  // revert scanner to move forward one character at a time, counting spaces.
		return false; // returns false if nothing found
	}

	// adds a pair of strings to a list of string pairs, if the first word of the pair is not already on the list
	public static void AddList( List<Pair<String, String>> list, String word, String output) throws Exception {
		Boolean found = false; // keeps track if we found the word already on the list
		int count = 0; 
		String Tokenword; // the second word of the pair that will be added to the list
		Iterator<Pair<String, String>> iterator = list.iterator(); // creates iterator for the list
		while (iterator.hasNext()) { // looks through the current list until the end
			Pair<String, String> W = iterator.next(); // gets the next pair on the list
			if (W.getL().equals(word)) {// if we find the first word of the pair we are adding is already on the list
				found = true;// marks this is on the list.
				//    System.out.println("Found " + output + " " + word + " again");
			}
		}
		if (!found) {// if we didn't find this pair already on the list
			count = list.size() +1; // how many words already found, plus one for this word
			Tokenword = " [" + output + count + "] "; // puts the output word together with count within brackets to create the token that will repace the first word of the pair
			Pair<String, String> newPair = new Pair<String, String>(word, Tokenword); // creates a new pair to add to the list
			list.add(newPair);// add the pair to the list.
			//  System.out.println("added " + word + " to the " + output + " list at count " + count);
		}  
	}

	// gets a file and a list of pairs of strings. Will replace every first word in that list of pairs, with the second word in that list of pairs
	public static void ReplaceList( List<Pair<String, String>> list, String file) throws Exception {

		Iterator<Pair<String, String>> iterator2 = list.iterator(); // makes iterator for the list of pairs
		while (iterator2.hasNext()) { // looks through the current list until the end
			Pair<String, String> W = iterator2.next(); // gets the next pair on the list
			FindReplace(file, "\\s" + W.getL() + "\\s", W.getR()); // finds and replaces the words of that pair in the file
		}

	}
	
	// tries to detect which language the file is written in.
	public static int WhichLang( String file) throws Exception {
		
		if (file.endsWith(".java")) { // if the file name extension is java it is probably a java file 
			System.out.print("Java file ");	
			return 1;
		}
		
		// detects C file name extensions
		if (file.endsWith(".c") || file.endsWith(".cpp") || file.endsWith(".h") || file.endsWith(".C") || file.endsWith(".cc") || file.endsWith(".cxx") || file.endsWith(".c++") ) {	
			System.out.print("C++ file ");
			return 2;
		}
		
		// detects file name extensions that probably mean this is not a regular text file, and trying to run this on the file will take a while
		if (file.endsWith(".pdf") || file.endsWith(".xml") || file.endsWith(".jpg")  || file.endsWith(".wav")) {
			System.out.println("This is not a text File.");	
			return 0;
		}
		
		File file2 = new File(file); //opens the output file 
		
	
		Scanner s = new Scanner(file2); // loads the output file into the scanner
		s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+");
		while (s.hasNext()) { // while there is another word in the file
			if (s.hasNext(".*\\#include") || s.hasNext("cout")) { s.close(); System.out.print("C++ file ");return 2;} // if it contains these common C++ words, it should be a C++ file.
			s.next(); // next word
		}
		s.close(); // close the scanner
		
		System.out.print("Java file ");	
		return 1; // assumes java if it fails to detect which language this file is by any of the above methods
	}

	
	// method accepts an input file to tokenize, an outputfile to save the tokenized file, and a boolean that should be true if you want to shorten the tokens by getting rid of spaces and brackets
	public static void tokenize(Boolean shorten, String inputFile, String outputFile) throws Exception {
		int langNum = WhichLang (inputFile); // integer represents which language to file is written in
		if(langNum != 0) {tokenize(shorten, langNum, inputFile, outputFile, "[CommentLine]", "[CommentBlock]", "[String]", "[SemiColin]", "[Dot]", "[Comma]", "[Class]", "[Bracket]", "[Logic]", "[VarType]", "[Loops]", "[Ifs]", "[KeyWord]", "[Compare]", "[Bool]", "[Math]");} // if it is not 0, which means it is not a non-text type of file, run the token code. DO NOT CHANGE THESE TOKEN NAMES OR IT CAN BREAK
	}

	//Main thread that gets called. booleans is true if you want to shorten the token output. integer is 1 to represent java and 2 to represent c++. 
	//Strings are file input, file output, and then what you want the token strings to be named. This was originally done to give flexibility to easily change token names to whatever is wanted, but token names should no longer be changed here
	public static void tokenize(Boolean shorten, int lang, String inputFile, String outputFile, String CommentLine, String CommentBlock, String stringQuotes, String SemiColin, String Dot, String Comma, String Classs, String Bracket, String Logic, String VarType, String Loops, String Ifs, String KeyWord, String Compare, String Bool, String Math) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(inputFile)); // makes new buffered reader
		String line; // stores the current line
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)); // makes a new buffered writer
		writer.write(" ");// adds a space to the start of the output file to avoid annoying bugs that can happen if there is an unknown word write at the start of the file, it won't be able to detect it because it expects a space both before and after it.

		String CommentBlockStartPattern = "\\/\\*.*"; // regex expression to match "/*", even if there are any other characters after it.
		String CommentBlockEndPattern = "\\*\\/.*";   // regex expression to match "*/", even if there are any other characters after it.
		Boolean inCommentBlock = false; // keeps track if we are in a comment block or not.

		while ((line = reader.readLine()) != null) { // will stop looping when there are no more lines to read
			
			if (line.length() == 0) { // if it is a blank line with nothing on it
				writer.newLine(); // Immediately go to the next line.
				continue;
			} 
			
			Scanner s = new Scanner(line); // loads scanner on the current line

			while (s.hasNext()) { // while there is another character in the current line

				if (inCommentBlock) { // if we are in a comment block, we only care about finding the end of the block
					s.useDelimiter(" "); // makes the scanner look for words in hasnext, not just single characters, so it can detect the end of the comment
					if (s.hasNext(CommentBlockEndPattern)) { // if the next part of the line is the characters to end a comment block
						s.close(); // close the scanner for the current line
						inCommentBlock = false; // set that we are no longer in a comment block
						break;
					} // if we find the end of block pattern, we can move on to the next line  
					else {
						RepeatNext(s, 1);
					} // moves the scanner forward one by one character to look for the end of the comment.
				} else { // checks for a lot of stuff when not in a comment block
					s.useDelimiter(" "); // sets it so the scanner will look at a word until the next space, not just one character	  

					if (s.hasNext("//.*")) { // if the next characters are "//" followed by any number of any characters
						writer.write(CommentLine); //write the commentline token
						s.close(); // stop looking at the current line and goes to the next one
						break; 
					} 
					if (s.hasNext(CommentBlockStartPattern)) { // looks for "/*" definined in regex above
						writer.write(CommentBlock); // writes commentblock token
						RepeatNext(s, 2); // moves forward 2 characters to get past /*
						inCommentBlock = true;  // marks that we are in a comment block
						while (s.hasNext()) { // loops until the end of the line. 
							s.useDelimiter(" "); // change back to looking at words because of RepeatNext setting it to characters. We can't find stuff with two or more characters when the scanner is set to only look forward one character, so we need to keep changing the delimiter. 
							if (s.hasNext(CommentBlockEndPattern)) {
								s.close();
								inCommentBlock = false;
								break;
							} // if we find the end of the comment, stops looking at the line and moves to the next one
							else {
								RepeatNext(s, 1);
							} // moves forward one character, didn't just write "next()" once here because that will move forward one word. 
						}
						break; // goes back to the start of the while loop. If the end of the comment block wasn't found, it will go the that if statement above for the next line.
					}

					s.useDelimiter("(\\b|\\B)"); // sets scanner to move forward one character at a time

					if (s.hasNext(" ")) {
						writer.write(s.next());  // if it is an empty space, print one space to output
					}
					else if (s.hasNext("\"")) { // detects quotation marks for strings. Assumes strings are on one line
						writer.write(stringQuotes); // writes the string token to output
						RepeatNext(s, 1); // moves past the quotation mark on input.
						while (s.hasNext()) { // loops until the end of the line.
							if (s.hasNext("\"")) { // if we find the ending quotation mark
								RepeatNext(s, 1); // moves past that quotation mark. 
								
								break; //  it exits the loop and continues searching the same line from where it left off
							}
							else {
								RepeatNext(s, 1);
							} // moves forward one character if the current character is not a quotation mark. 
						}
					} 
					/*
					if (lang == 2) {
						s.useDelimiter("[\\s\\(\\[\\{\\)\\}\\]]+");
						if (s.hasNext("\\-\\>.*" )){ RepeatNext(s,"->".length()); writer.write("[Arrow]"); }   // if it is a C file, detect the arrows and replace with a token
						s.useDelimiter("(\\b|\\B)");
					} */
					
					else if (s.hasNext("\\;")) {   // looks for semicolins to replace with a token
						writer.write(SemiColin); 
						s.next();
					}
					
					else if (s.hasNext("\\:")) { // looks for colins to replace with a token
						writer.write("[Colin]");
						s.next();
					}
					else if (s.hasNext("\\.")) { // looks for "." to replace with a token
						writer.write(Dot);
						s.next();
					} 
					else if (s.hasNext("\\,")) { // looks for "," to replace with a token
						writer.write(Comma);
						s.next();
					} 
					else if (CheckArray(s)) { // Looks for array positions to replace with a token
						writer.write("[Array]");
					} 
					else if (CheckBracket(s)) { // Looks for brackets to replace with a token
						writer.write(Bracket);
					} 

					else if (CheckVarType(lang,s)) { // looks for variable type names to replace with a token
						writer.write(VarType);
					} 
					else if (CheckComparators(s)) { // looks for comparators to replace with a token
						writer.write(Compare);
					} 
					else if (CheckLogic(s)) { // looks for logic characters to replace with a token
						writer.write(Logic);
					} 			
					else if (CheckBitWise(s)) { // Looks for bitwise symbols to replace with a token
						writer.write("[Bitwise]");
					} 
					else if (CheckOutput(lang, s)) { // Looks for output words to replace with a token
						writer.write("[Output]");
					} 
					else if (CheckLoops(s)) { // looks for loop commands to replace with a token
						writer.write(Loops);
					} 
					else if (CheckIfs(s)) { // looks for if commands to replace with a token
						writer.write(Ifs);
					} 
					else if (CheckClass(s)) { // looks for the word class to replace with a token
						writer.write(Classs);                        
					} 
					else if (CheckReturn(s)) { // looks for the word return to replace with a token
						writer.write("[Return]");
					} 
					else if (CheckKeywords(lang,s)) { // looks for various keywords to replace with a token
						writer.write(KeyWord);
					} 
				
					else if (CheckBoolean(s)) { // looks for boolean symbols to replace with a token
						writer.write(Bool);
					} 
					else if (CheckMath(s)) { // looks for math symbols to replace with a token
						writer.write(Math);
					} 
					else {
						writer.write(s.next());  // if nothing is found, write the next character to the output, and move forward one character on the input scanner. 
					} 
				}
			}
			s.close(); // closes the scanner for the current line
			writer.newLine(); // goes to the next line in the output file
		}

		reader.close(); // closes reader and writer after it has looped through every line of the input
		writer.close();

		FindReplace(outputFile, "\t", " "); // replaces tabs with spaces
		FindReplace(outputFile, "\\[", " \\["); // puts a space in front of every token
		FindReplace(outputFile, "\\]", "\\] "); // puts a space behind every token
		FindReplace(outputFile, "\\/", ""); // removes / because it causes trouble with java thinking it is getting regex commands from the text
		FindReplace(outputFile, "\\\\", ""); // removes \ because it causes trouble with java thinking it is getting regex commands from the text

		// replaces variable names
		File file2 = new File(outputFile); //opens the output file with all the tokens added so far
		String word2; // stores a word that is getting added to a list
		List<Pair<String, String>> varList = new ArrayList<Pair<String, String>>(); // list of pairs of strings. First word gets replaced with the second word. First word is a variable name list, second word is a token name with a number in it.
		List<Pair<String, String>> classList = new ArrayList<Pair<String, String>>();// list of pairs of strings. First word gets replaced with the second word. First word is a variable class name list, second word is a token name with a number in it.
		List<Pair<String, String>> classInstanceList = new ArrayList<Pair<String, String>>();// list of pairs of strings. First word gets replaced with the second word. First word is a variable class instance list, second word is a token name with a number in it.

		Scanner s3 = new Scanner(file2); // loads the output file into the scanner

		while (s3.hasNext()) { // while there is another character in the current line
			if (s3.hasNext("\\[VarType\\]")) { // if we are at a variable type token
				s3.next(); // move past that token
				if (s3.hasNext("\\[.*")) { // look at if what is after the vartype token is also a token. (it starts with a square bracket)
					s3.next(); // if so, move past this
				} else {
					word2 = s3.next(); // if what is after a variable type token is not known, then it is probably a variable name
					AddList(varList, word2, "Name"); // add this name to the list if it is not already on it.
				}
			} else if (s3.hasNext("\\[Class\\]")) { // repeats the above logic, except with the class token and a seperate list.
				s3.next();
				if (s3.hasNext("\\[.*")) {
					s3.next();
				} else {
					word2 = s3.next();
					AddList(classList, word2, "ClassName");
				}
			}
			
			else {
            	s3.next(); // if it isn't a vartype token or a class token, move to the next word
            }
		}
		ReplaceList(varList, outputFile); // use find and replace with this list on the file. The first word in each pair gets replaced with the second word of that pair.
		ReplaceList(classList, outputFile); 

		s3.close(); // closes the reader. 

		Scanner s4 = new Scanner(file2); // loads the output file into a new scanner

		while (s4.hasNext()) { // while there is another character in the current line
			if (s4.hasNext("\\[ClassName.*")) { // repeats the above logic, except with a classname token.
				s4.next();
				if (s4.hasNext("\\[.*")) {
					s4.next();
				} else {
					word2 = s4.next();
					AddList(classInstanceList, word2, "ClassInstance");
				}
			} else {
				s4.next();
			}
		}

		ReplaceList(classInstanceList, outputFile); // use find and replace with this list on the file. The first word in each pair gets replaced with the second word of that pair.
		s4.close(); // closes the reader. 

		File file = new File(outputFile); // opens the file again
		String word; //stores a word to add to the number or character list
		String wordU; // stores a word to add to the unknown list


		Scanner s2 = new Scanner(file); // loads the file into the scanner
		List<Pair<String, String>> numberList = new ArrayList<Pair<String, String>>();// list of pairs of strings. First word gets replaced with the second word. First word is a number found in the file, second word is a token name with a number in it.
		List<Pair<String, String>> unkownList = new ArrayList<Pair<String, String>>();// list of pairs of strings. First word gets replaced with the second word. First word is a character found in the file, second word is a token name with a number in it.
		List<Pair<String, String>> charList = new ArrayList<Pair<String, String>>();// list of pairs of strings. First word gets replaced with the second word. First word is an unknown word found in the file, second word is a token name with a number in it.
		while (s2.hasNext()) { // while there is another character in the current line
			if (s2.hasNext("\\[.*")) { // if the next word is a token. It starts with [
				s2.next(); // move to the next word
			} 
			else {
				if (s2.hasNext("\\d*")) { // if the next word is made of digits 
					word = s2.next(); 
					AddList(numberList, word , "Number"); //add that string of digits to the list of numbers, , if it isn't already there.
				}
				else if (s2.hasNext("\\'.\\'")) { // if the next word is a single character between two ' 
					word = s2.next();
					AddList(charList, word , "Char"); //add that string representing a character to the list of characters, if it isn't already there.
				}
				else { // if we don't know what it is
					wordU = s2.next(); 
					AddList(unkownList, wordU , "Unknown"); } // add this string to the list of unkown strings, if it isn't already there.

			} 
		}

		ReplaceList(charList, outputFile); // goes through the three lists and replaces every first string in the pair with the second string in the pair, in the output file of tokens.
		ReplaceList(numberList, outputFile);
		ReplaceList(unkownList, outputFile);

		s2.close(); // closes the reader. 


		if (shorten) { // if the input for this variable is true we want the token output to be in the shortened version

			FindReplace(outputFile, "\\[CommentLine]", ""); // removes this token from the output file.
			FindReplace(outputFile, "\\[CommentBlock]", ""); // removes this token from the output file.
			FindReplace(outputFile, "\\[SemiColin]", ""); // removes this token from the output file.
			FindReplace(outputFile, "\\[Colin]", ""); // removes this token from the output file.
			FindReplace(outputFile, "\\[Bracket]", ""); // removes this token from the output file.
			/* 
			// EDIT TOKENS TO BECOME ANYTHING YOU WANT
			FindReplace(outputFile, "\\[CommentLine]", "CL"); 
			FindReplace(outputFile, "\\[CommentBlock]", "CB"); 
			FindReplace(outputFile, "\\[String]", "St"); 
			FindReplace(outputFile, "\\[SemiColin]", "SC"); 
			FindReplace(outputFile, "\\[Dot]", "D"); 
			FindReplace(outputFile, "\\[Comma]", "Co"); 
			FindReplace(outputFile, "\\[Class]", "Cl"); 
			FindReplace(outputFile, "\\[Bracket]", "Br"); 
			FindReplace(outputFile, "\\[Logic]", "Lo"); 
			FindReplace(outputFile, "\\[VarType]", "VT"); 
			FindReplace(outputFile, "\\[Loops]", "Lo"); 
			FindReplace(outputFile, "\\[Ifs]", "If"); 
			FindReplace(outputFile, "\\[KeyWord]", "KW"); 
			FindReplace(outputFile, "\\[Compare]", "Cr"); 
			FindReplace(outputFile, "\\[Bool]", "Bo");
			FindReplace(outputFile, "\\[Math]", "Ma");
			FindReplace(outputFile, "\\[Name", "Na");
			FindReplace(outputFile, "\\[ClassName", "CN");
			FindReplace(outputFile, "\\[ClassInstance", "CI");
			FindReplace(outputFile, "\\[Unknown", "Un");
			FindReplace(outputFile, "\\[Number", "#");
			FindReplace(outputFile, "\\]", ""); */

			FindReplace(outputFile, " ", ""); // removes all spaces between tokens
			FindReplace(outputFile, "\\[", ""); // removes [
			FindReplace(outputFile, "\\]", ""); // removes ]
		}
		System.out.println("done " + outputFile);

	}

}
