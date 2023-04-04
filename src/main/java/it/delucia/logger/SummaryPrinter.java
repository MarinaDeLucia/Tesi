package it.delucia.logger;

import it.delucia.Settings;
import it.delucia.model.Job;
import it.delucia.model.ModelLoader;
import it.delucia.model.Resource;
import it.delucia.model.events.JobArrival;
import it.delucia.model.events.ResourceLoad;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SummaryPrinter {
    //singleton pattern
    private static SummaryPrinter instance = null;
    private final String separator = "===============================================================";

    private SummaryPrinter() {
    }

    public static SummaryPrinter getInstance() {
        if (instance == null) {
            instance = new SummaryPrinter();
        }
        return instance;
    }

    //print a start message to a string buffer and then pass it to the SummaryLogger
    public void start(String sessionName) {
        SummaryLogger.getInstance().start(sessionName);
        StringBuilder sb = new StringBuilder();
        //create a date formatted like 04 mar 2020 12:00:00
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        sb.append(separator).append("\n");
        sb.append("Session: ").append(sessionName).append(" at ").append(formattedDate).append("\n");
        sb.append(separator).append("\n");
        SummaryLogger.getInstance().append(sb.toString());

        //generate a string with global settings:
        StringBuilder settings = new StringBuilder();
        settings.append("GLOBAL SETTINGS:").append("\n");
        settings.append("  - Number of extracted jobs: ").append(Settings.NUMBER_OF_EXTRACTED_JOBS).append("\n");
        settings.append("  - Max step: ").append(Settings.MAX_STEP).append("\n");
        SummaryLogger.getInstance().append(settings.toString());

    }

    public void printInitialPlan(){
        StringBuilder sb = new StringBuilder();
        sb.append("INITIAL PLAN:").append("\n");
        sb.append("  - Number of jobs: ").append(ModelLoader.getInstance().getJobs().size()).append("\n");
        sb.append("  - Number of machines: ").append(ModelLoader.getInstance().getNumberOfMachines()).append("\n");
        sb.append("  - Number of resources: ").append(ModelLoader.getInstance().getResources().size()).append("\n");
        sb.append("  - Number of extra jobs: ").append(ModelLoader.getInstance().getNumberOfExtraJobs()).append("\n");
        sb.append("------------------------------------------------------------------").append("\n");
        sb.append("RESOURCE DETAILS:").append("\n");
        for(Resource r : ModelLoader.getInstance().getResources()){
            sb.append("  - [").append(r.getName()).append("]\t with initial quantity of ").append(r.getQuantity()).append("\n");
        }
        sb.append("\n");
        sb.append("RESOURCE LOADS:").append("\n");
        Map<Integer, List<ResourceLoad>> resourceLoadMapByStep = ModelLoader.getInstance().getResourceLoadMapByStep();
        //print a result like this, ordering the result by the step (the key)
        //step 1: [resource1] +10, [resource2] -5, [resource3] +2
        //step 2: [resource1] -10, [resource2] +5, [resource3] -2
        //first get the key set and order them
        List<Integer> steps = resourceLoadMapByStep.keySet().stream().sorted().toList();
        for(Integer step : steps){
            sb.append("  - step ").append(step).append(": ");
            List<ResourceLoad> resourceLoads = resourceLoadMapByStep.get(step);
            for(ResourceLoad rl : resourceLoads){
                sb.append("[").append(rl.getResource().getName()).append("] ");
                if(rl.getQuantity() > 0){
                    sb.append("+");
                }
                sb.append(rl.getQuantity()).append(", ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("JOB DETAILS:").append("\n");
        //for each job print the job and the resources it consumes in the following format: (the value of the consumption is the quantity with - sign)
        //[J1] which consumes [resource1] -10, [resource2] -5, [resource3] -2
        for(Job job : ModelLoader.getInstance().getJobs()){
            sb.append("  - [").append(job.printId()).append("] which consumes ");
            for(Resource r : ModelLoader.getInstance().getResources()){
                int quantity = job.getConsumptionByResource(r);
                if(quantity != 0){
                    sb.append("[").append(r.getName()).append("] -");
                    sb.append(quantity).append(",\t ");
                }
            }
            sb.append("\n");
        }

        sb.append("\n");
        sb.append("NEW JOBS ARRIVAL DETAILS:").append("\n");
        Map<Integer, List<JobArrival>> jobArrivalMapByStep = ModelLoader.getInstance().getJobArrivalMapByStep();
        //first, order the keys into a list
        List<Integer> arrivalSteps = jobArrivalMapByStep.keySet().stream().sorted().toList();
        //for each step print the job arrivals in the following format:
        //step 1: [J1] which consumes [resource1] +10, [resource2] -5, [resource3] +2
        //        [J2] which consumes [resource1] -10, [resource2] +5, [resource3] -2
        //step 2: [J3] which consumes [resource1] +10, [resource2] -5, [resource3] +2

        for(Integer step : arrivalSteps){
            sb.append("  - step ").append(step).append(": ");
            List<JobArrival> jobArrivals = jobArrivalMapByStep.get(step);
            for(JobArrival ja : jobArrivals){
                sb.append("[").append(ja.getJob().printId()).append("] which consumes ");
                for(Resource r : ModelLoader.getInstance().getResources()){
                    int quantity = ja.getJob().getConsumptionByResource(r);
                    if(quantity != 0){
                        sb.append("[").append(r.getName()).append("] -");
                        sb.append(quantity).append(", ");
                    }
                }
                sb.append("\n");
            }
        }
        SummaryLogger.getInstance().append(sb.toString());
    }



    //end the summary by calling the close/end method of the SummaryLogger
    public void end() {
        //append the ending time in the format like 04 mar 2020 12:00:00
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        SummaryLogger.getInstance().append(separator);
        SummaryLogger.getInstance().append("Session ended at " + formattedDate);
        SummaryLogger.getInstance().append(separator);

        SummaryLogger.getInstance().close();
        //open the file by the default application of the OS
        try {
            Desktop.getDesktop().open(SummaryLogger.getInstance().getCurrentLogFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //append a info message to the SummaryLogger
    public void info(String message) {
        SummaryLogger.getInstance().append("[INFO] " + message);
    }

    //append a warning message to the SummaryLogger
    public void warning(String message) {
        SummaryLogger.getInstance().append("[WARNING] " + message);
    }

    //append a error message to the SummaryLogger
    public void error(String message) {
        SummaryLogger.getInstance().append("[ERROR] " + message);
    }


    public void printStartBanner(){
        StringBuilder sb = new StringBuilder();
        sb.append(separator).append("\n\n");
        sb.append("                  S T A R T I N G   E X E C U T I O N").append("\n\n");
        sb.append(separator).append("\n");
        SummaryLogger.getInstance().append(sb.toString());
    }
}
