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

import au.com.bytecode.opencsv.*;

import java.util.List;

public class IDFHelper
{
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
						if (line.contains("Construction,"))
					{	
						StringBuilder lineBuilder = new StringBuilder("Construction,");
						String nextLine = br.readLine();
						lineBuilder.append("\n" + nextLine + "\n");
						if(nextLine.toLowerCase().contains("wall"))
						{
							String currentLine = null;
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

		System.out.println(newFile.getName() + oldFile.getName());
		return oldFile;
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