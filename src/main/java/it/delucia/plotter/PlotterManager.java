package it.delucia.plotter;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.delucia.logger.SummaryLogger;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.IOException;

public class PlotterManager {

    private Schedule schedule;
    private String fileName;
    private String folderName;

    private PlotterManager(){
        //private constructor
    }


    public static class Builder{
        private Schedule schedule;
        private String fileName;
        private String folderName;

        public Builder(){
            //empty constructor
        }

        public Builder prepare(Schedule schedule){
            this.schedule = schedule;
            return this;
        }

        public Builder setFileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public Builder setFolderName(String folderName){
            this.folderName = folderName;
            return this;
        }

        public PlotterManager build(){
            PlotterManager plotterManager = new PlotterManager();
            plotterManager.schedule = this.schedule;
            plotterManager.fileName = this.fileName;
            plotterManager.folderName = this.folderName;
            return plotterManager;
        }
    }


    /**
     * Create a file with name fileName and print the schedule in JSON format using Jackson library
     */
    public void printJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            //check if folder exists
            File folder = new File(folderName);
            if(!folder.exists()){
                folder.mkdir();
            }
            String sessionName = SummaryLogger.getInstance().getSessionName();
            //Create a subfolder with the name of the session
            File sessionFolder = new File(folderName + "/" + sessionName);
            if(!sessionFolder.exists()){
                sessionFolder.mkdir();
            }
            //create the file
            mapper.writeValue(new File(folderName + "/" + sessionName + "/" + fileName + ".json"), schedule);
            mapper.writeValue(new File("script/jobs.json"), schedule);
            Process p = new ProcessBuilder("python", "script/gantt.py", "none").start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.execfile("script/gantt.py");




    }
}
