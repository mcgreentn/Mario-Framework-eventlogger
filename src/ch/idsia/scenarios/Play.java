package ch.idsia.scenarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import competition.cig.robinbaumgarten.AStarAgent;
//import competition.cig.mechanicextractor.AStarAgent;
//import competition.cig.robinbaumgarten.AStarAgent;
import competition.cig.robinbaumgarten.EnemyBlindAgent;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.level.Level;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationOptions;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */
public class Play {

    public static void main(String[] args) {
	GlobalOptions.VisualizationOn = true;
	GlobalOptions.MarioCeiling = true;
	
        Agent controller = new AStarAgent();
        if (args.length > 0) {
            controller = AgentsPool.load (args[0]);
            AgentsPool.addAgent(controller);
        }
        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(controller);
        Task task = new ProgressTask(options);
        options.setMaxFPS(false);
        options.setVisualization(true);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setMarioMode(0);
        
        task.setOptions(options);
        
        getAllLevelMechanics(task, controller);
        
    }
    
    
    public static void getAllLevelMechanics(Task task, Agent controller) {
		ArrayList<Path> allFiles = new ArrayList<Path>();
	    try {
			Files.find(Paths.get("./levels"), 999, (p, bfa) -> bfa.isRegularFile()).forEach(allFiles::add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    for(Path file : allFiles) {
	    	if(file.toString().contains(".txt")){
	    		Level level = Play.getFileChrome(file);
	    		((ProgressTask) task).evaluate(controller, level);
	    	}
			String name = file.toString();
			System.out.println(name);
	    }
    }
    
    private static Level getFileChrome(Path file) {
		BufferedReader reader;
		String chrome = "";
    	try {
			reader = new BufferedReader(new FileReader(file.toString()));
			String line = reader.readLine();
			chrome = line;
			line = reader.readLine();
			while (line != null) {

				chrome = chrome + "\n" +  line;
				// read next line
				line = reader.readLine();
			}
			System.out.println(chrome);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Level level = Level.initializeLevel(chrome, 3, false);
    	return level;
    }
}
