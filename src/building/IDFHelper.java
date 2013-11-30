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

public class IDFHelper
{
	public static void main(String argv[])
	{
		double[] genome = {90.000, 60.000};
		modifyIDF(genome);
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
					if (line.contains("!- North Axis"))
					{	
						line = line.replace(line, "\t"+angle+",\t!- North Axis");
						System.out.println(line);
					}

					bw.write(line+"\n");
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
}