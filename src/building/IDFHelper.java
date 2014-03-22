
package building;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import au.com.bytecode.opencsv.*;

import java.util.List;

public class IDFHelper
{
	public static final String[] insulation_materials = {"Material1", "Material2", "Material3", "Material4"};
	public static void main(String argv[])
	{
		double[] genome = {90.000, 60.000};
		//modifyIDF(genome);
		parseBuildingCSV();
	}
	/**
	* @param genome an array representing the genome of the building
	* @return the modified IDF File
	*/
	public static File modifyIDF(double[] genome)
	{
		int numDecisionVars = genome.length;
		double angle = genome[0];

		String oldFileName = "bentley.idf";
		String tmpFileName = "bentley.idf.temp";

		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			try 
			{
				br = new BufferedReader(new FileReader(oldFileName));
				bw = new BufferedWriter(new FileWriter(tmpFileName));
				String line;

				while ((line = br.readLine()) != null) 
				{
					//If we reach HVAC templates, break out of the loop. They will be appended later
					if(line.contains("!- Begin HVAC Zones and System")) break;

					//Otherwise, proceed with replacing wall insulation 
					if (line.contains("Construction,"))
					{	
						StringBuilder lineBuilder = new StringBuilder("Construction,");
						String nextLine = br.readLine();
						lineBuilder.append("\n" + nextLine + "\n");
						if(nextLine.toLowerCase().contains("wall"))
						{
							String currentLine = null;
							String insulation_material = "\t" + insulation_materials[(int) genome[0]] 
															+ ";\t\t\t!- Layer 3"; // Assuming genome[0] is always correct
							do{
								currentLine = br.readLine();
								if(currentLine.contains("!- Layer 3"))
								{
									
									if(currentLine.contains(";"))
									{
										currentLine = currentLine.replace(currentLine, "\tIN46;\t\t\t!- Layer 3");	
									}
									else
									{
										currentLine = currentLine.replace(currentLine, "\tIN46,\t\t\t!- Layer 3");		
									}
									
									System.out.println(currentLine);
								}
								lineBuilder.append(currentLine);
								lineBuilder.append("\n");

							}while(!currentLine.contains(";"));
						}
						bw.write(lineBuilder.toString());
					}
					// Or and window glazing.
					else if (line.contains(""))
					{
						
					}
					else
					{
						bw.write(line+"\n");
					}
					
				}
			}
			finally {
				if(br != null)
					br.close();
				if(bw != null)
					bw.close();		
			}	
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		
      // Once everything is complete, delete old file..
		File oldFile = new File(oldFileName);
		oldFile.delete();

      // And rename tmp file's name to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile); 

		// Second pass - HVAC system and Set points
		// Read the right file using genome[2] and read it into a string
		// Append the string to the file
		try
		{
			String hvacType = "hvac" + genome[2];
    		FileWriter fw = new FileWriter(oldFileName,true); //the true will append the new data
    		fw.write(readFile(hvacType,Charset.defaultCharset()));//appends the string to the file
    		fw.close();
    	}
    	catch(IOException ioe)
    	{
    		System.err.println("IOException: " + ioe.getMessage());
    	}

		System.out.println(newFile.getName() + oldFile.getName());
		return oldFile;
	}

	/**
	* @param path path to the file to be read
	* @param encoding Charset encoding usually set to default.
	* @return the file in a String
	*/
	static String readFile(String path, Charset encoding) throws IOException 
    {
    	byte[] encoded = Files.readAllBytes(Paths.get(path));
    	return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }
	/**
	* Parse the building.csv file return the columns
	* 
	*/
	public static String[] parseBuildingCSV()
	{
		String[] lastRow = null;
		try
		{
			CSVReader reader = new CSVReader(new FileReader("Output/bentley.csv"), ',', '\"', 1);
			String [] nextLine;
			List csvRows = reader.readAll();
			lastRow = (String[]) csvRows.get(csvRows.size()-1);
			double electricity = Double.parseDouble(lastRow[1]);
          	double natural_gas = Double.parseDouble(lastRow[16]);

          	System.out.println("Total Electricity in Joules : " + lastRow[1] +"\t" +electricity);
          	System.out.println("Total Natural Gas in Joules : " + lastRow[16] +"\t"+ natural_gas);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return lastRow;
		
	}


}