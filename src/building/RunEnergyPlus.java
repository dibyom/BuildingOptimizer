package building;
import org.apache.commons.exec.*;
import java.lang.Runtime;
import java.io.*;

public class RunEnergyPlus
{
	public void execute() throws java.io.IOException
	{
		CommandLine cmdLine = new CommandLine("runenergyplus");
		cmdLine.addArgument("bentley.idf");
		cmdLine.addArgument("USA_IL_Chicago-OHare.Intl.AP.725300_TMY3.epw");
		
		// cmdLine.addArgument("${file}");
		// HashMap map = new HashMap();
		// map.put("file", new File("invoice.pdf"));
		// commandLine.setSubstitutionMap(map);

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

		ExecuteWatchdog watchdog = new ExecuteWatchdog(600*1000);
		Executor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);
		executor.execute(cmdLine, resultHandler);

		// some time later the result handler callback was invoked so we
		// can safely request the exit value
		int exitValue = resultHandler.getExitValue();
		System.out.println(exitValue);	
	}
}