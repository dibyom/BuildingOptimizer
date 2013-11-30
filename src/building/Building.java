package building;

import ec.util.*;
import ec.*;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.*;
import ec.vector.*;

import java.io.File;
import java.io.IOException;
public class Building extends Problem implements SimpleProblemForm {

	public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
	{
		if( !( ind instanceof DoubleVectorIndividual ) )
            state.output.fatal( "The individuals for this problem should be DoubleVectorIndividuals." );

        DoubleVectorIndividual temp = (DoubleVectorIndividual)ind;
        double[] genome = temp.genome;
        int numDecisionVars = genome.length;
        if(numDecisionVars!=1) throw new RuntimeException("Building needs exactly 1 decision variables (genes).");

        float[] objectives = ((MultiObjectiveFitness)ind.fitness).getObjectives();
  
        try
        {
          File idf = IDFHelper.modifyIDF(genome);
          RunEnergyPlus.execute(idf);
        }
        catch(IOException e)
        {
          e.printStackTrace();
        }
        
        
        
       	
        objectives[0] = (float) 1;
       	objectives[1] = (float) 2;

       	((MultiObjectiveFitness)ind.fitness).setObjectives(state, objectives);
        ind.evaluated = true;
	}
	
  public static void modify_idf(float angle)
  {
    //Open Bentley.idf
    //Go to line 174
    //Create a string to replace it
    //Save File 
  }
}