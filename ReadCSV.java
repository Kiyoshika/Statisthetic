
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class ReadCSV {
    public ReadCSV(String fileName) throws FileNotFoundException, IOException {
		
    }
    
    public String[][] Read(String fileName) throws FileNotFoundException, IOException {
        int totalRows = CountRows(fileName);
		int totalCols = CountColumns(fileName);
		
		FileReader fr = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fr);
		
		String line;
		String data[][] = new String[totalRows][totalCols];
		
		int currentRow = 0; //row - 1
		int currentCol = 0; //col - 1
		
		int startPos = 0;
		int commaPos = 0;
		String subtext;
		
		while ((line = reader.readLine()) != null) {
			for (int i = 0; i < line.length(); i++) {
				//last column
				if (i == line.length()-1) {
					subtext = line.substring(startPos,line.length());
					data[currentRow][currentCol] = subtext;
					//reset values for next row
					startPos = 0;
					currentCol = 0;
					currentRow++;
				}
				else if (line.charAt(i) == ',') {
					commaPos = i;
					subtext = line.substring(startPos,commaPos);
					data[currentRow][currentCol] = subtext;
					startPos = commaPos+1;
					currentCol++;
				}
			}
		}
		reader.close();
                
                return data;
    }
    
    //Count rows
    public int CountRows(String path) throws FileNotFoundException {
        FileReader f = new FileReader(path);
        BufferedReader br = new BufferedReader(f);
        
        int counter = 0;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                counter++;
            }
            
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return counter;
        
    }
    
    //Count columns
    public int CountColumns(String fileName) throws IOException {
		
		FileReader fr = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fr);
		
		int colCounter = 0;
		String line;
		//read only first line
		line = reader.readLine();
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ',') { colCounter++; }
		}
		reader.close();
		return colCounter+1;
	}
}
