import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Test {
	
	static String [] stopWords;
	static HashTable table = new HashTable();

	public static void main(String[] args) throws Exception
	{
		
		stopWords = readingFileLineByLine("stop_words_en.txt").split("/");

		final File folder = new File("bbc");
		listFilesForFolder(folder);

		int size = 0;
		System.out.println("Tablesize: "+table.getTABLE_SIZE());
		for (int j = 0; j < table.getTable().length; j++) {
			if(table.getTable()[j] != null && table.getTable()[j].getKey() != " ")
				size++;
		}
		System.out.println("Size:" +size);
		System.out.println("Collisions: "+table.collisionCount);
		File file = new File("search.txt");
		Scanner sc=new Scanner(file);
		/*
		while(sc.hasNextLine()) {
			String line =sc.nextLine();
			if(!line.equals(""))
			{
				System.out.println(line);
				System.out.println(table.get(table.search(line)));
			}
				
		}*/

	
	}
	public static void operationsForOneText(String text, String fileName) throws Exception {
		String [] splitted = splitting(text);

		// Deleting stopWords in splitted words
		for (int i = 0; i < splitted.length; i++) {
			for (int j = 0; j < stopWords.length; j++) {
				if(splitted[i].equals(stopWords[j])) {
					splitted[i] = "";
					break;
				}
			}
		}
		
		for (int i = 0; i < splitted.length; i++) {

			if(splitted[i].length() > 0) {
				int hash = table.hashValue(splitted[i]);
				table.put(hash,splitted[i], fileName);
			}
		}	
	}
	
	public static void listFilesForFolder(final File folder) throws Exception {
		String text = "";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(folder.getName() + "/" + fileEntry.getName());
				text = readingFile(fileEntry);
				operationsForOneText(text, fileEntry.getName());
			}
		}
	}

	public static String readingFile(File fileEntry) throws IOException {

		String text = "";

		try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
			String line;
			while ((line = br.readLine()) != null) {
				text = text + line + "/";
			}
		}

		return text;
	} 

	public static String readFileAsString(String fileName)throws Exception 
	{ 
		String data = ""; 
		data = new String(Files.readAllBytes(Paths.get(fileName))); 
		return data; 
	} 


	public static String[] splitting(String data) throws Exception
	{
		String DELIMITERS = "[-+=" +

		        " " +        //space

		        "\r\n " +    //carriage return line fit

				"1234567890" + //numbers

				"’'\"" +       // apostrophe

				"(){}<>\\[\\]" + // brackets

				":" +        // colon

				"," +        // comma

				"‒–—―" +     // dashes

				"…" +        // ellipsis

				"!" +        // exclamation mark

				"." +        // full stop/period

				"«»" +       // guillemets

				"-‐" +       // hyphen

				"?" +        // question mark

				"‘’“”" +     // quotation marks

				";" +        // semicolon

				"/" +        // slash/stroke

				"⁄" +        // solidus

				"␠" +        // space?   

				"·" +        // interpunct

				"&" +        // ampersand

				"@" +        // at sign

				"*" +        // asterisk

				"\\" +       // backslash

				"•" +        // bullet

				"^" +        // caret

				"¤¢$€£¥₩₪" + // currency

				"†‡" +       // dagger

				"°" +        // degree

				"¡" +        // inverted exclamation point

				"¿" +        // inverted question mark

				"¬" +        // negation

				"#" +        // number sign (hashtag)

				"№" +        // numero sign ()

				"%‰‱" +      // percent and related signs

				"¶" +        // pilcrow

				"′" +        // prime

				"§" +        // section sign

				"~" +        // tilde/swung dash

				"¨" +        // umlaut/diaeresis

				"_" +        // underscore/understrike

				"|¦" +       // vertical/pipe/broken bar

				"⁂" +        // asterism

				"☞" +        // index/fist

				"∴" +        // therefore sign

				"‽" +        // interrobang

				"※" +          // reference mark

		        "]";

		String[] splitted = data.toLowerCase().split(DELIMITERS);

		return splitted;
	}

	public static String readingFileLineByLine(String fileName) throws FileNotFoundException, IOException {
		String text = "";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				text = text + line + "/";
			}
		}

		return text;
	}
}
