package building;

import ec.util.*;
import ec.*;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.*;
import ec.vector.*;

import java.io.File;
import java.io.IOException;

public class Building extends Problem implements SimpleProblemForm {

	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation, final int threadnum)
	{
		
    //Ensure that the individual is of right type
    if( !( ind instanceof DoubleVectorIndividual ) )
      state.output.fatal( "The individuals for this problem should be DoubleVectorIndividuals." );

    float[] objectives = ((MultiObjectiveFitness)ind.fitness).getObjectives();
    // Copy of ind
    DoubleVectorIndividual temp = (DoubleVectorIndividual)ind;
    double[] genome = temp.genome;

    // Ensure the number of decision variables is 4
    int numDecisionVars = genome.length; 
    if(numDecisionVars!=1) throw new RuntimeException("Building needs exactly 1 decision variables (genes).");

    
    // * Create a modified IDF with the genomes and run E+ on the modified file.
    // * @TODO change the method name to something more relevant
    // * @TODO Have the execute return the output file.
     
    try
    {
      //File idf = IDFHelper.modifyIDF(genome);
      //RunEnergyPlus.execute(idf);
      //File file = RunEnergyPlus.execute(idf);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    // Get cost and energy usage values from the building. 
    String[] lastRow = IDFHelper.parseBuildingCSV();
    double electricity = Double.parseDouble(lastRow[1]);
    double natural_gas = Double.parseDouble(lastRow[16]);
    double energy = electricity + natural_gas;
    double cost = 1 * electricity + 0.7 * natural_gas;

    //Set the objectives 
    objectives[0] = (float) energy; 
    objectives[1] = (float) cost;

    ((MultiObjectiveFitness)ind.fitness).setObjectives(state, objectives);
    ind.evaluated = true;
  }

  public static void compute_costs()
  {

  }
}