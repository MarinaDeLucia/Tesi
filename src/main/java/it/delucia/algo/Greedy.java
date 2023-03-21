package it.delucia.algo;

import it.delucia.model.Job;
import it.delucia.model.Machine;

import java.util.List;

public class Greedy {
    //implement singleton pattern
    private static Greedy instance = null;
    private List<Job> jobs = null;
    private boolean initialized = false;
    private List<Machine> machines = null;

    private Greedy() {
    }

    public static Greedy getInstance() {
        if (instance == null) {
            instance = new Greedy();
        }
        return instance;
    }

    public void init(List<Job> jobs, List<Machine> machines) {
        this.jobs = jobs;
        initialized = true;
        this.machines = machines;
    }

    //implement greedy algorithm
    public void run() {
        if(!initialized) {
            throw new RuntimeException("Greedy not initialized");
        }
        destruction();
    }

    //print all jobs
    public void printJobs() {
        if(!initialized) {
            throw new RuntimeException("Greedy not initialized");
        }
        for(Job job: jobs) {
            System.out.println("Greedy: job -> "+job);
        }
    }

    public void destruction(){
        System.out.println("Greedy: destruction");
        System.out.println("Initial jobs list");
        printJobs();
        //if size of the list is <= 2 then throw an exception
        if(jobs.size() <= 2) {
            throw new RuntimeException("Greedy: jobs list size is <= 2");
        }
        //extract the first two jobs
        Job job_extracted_1 = this.jobs.remove(this.jobs.size() - 1);
        Job job_extracted_2 = this.jobs.remove(this.jobs.size() - 1);

        //print the extracted jobs
        System.out.println("Greedy: extracted jobs -> "+job_extracted_1+" "+job_extracted_2);

        //print the remaining jobs
        System.out.println("Greedy: remaining jobs");
    }

    private void localSearch(List<Job> jobs){
        System.out.println("Greedy: local search");
        //evaluate which is the best execution order of the remaining jobs. The evaluation is based on the makespan
        if(jobs.size() < 2) {
            throw new RuntimeException("Greedy: jobs list size is <= 2");
        }else if(jobs.size() == 2){
            //evaluate the makespan of the two jobs in the current order
            Job job_extracted_1 = jobs.get(0);
            Job job_extracted_2 = jobs.get(1);
            //                              M1                                                      M2
            //                               2                                                       1
            int makespan_1 = job_extracted_1.getProcessingTime(1) + job_extracted_2.getProcessingTime(1);  // 3
            //                               6                                                       3
            int makespan_2 = job_extracted_1.getProcessingTime(2) + job_extracted_2.getProcessingTime(2);
            


        }
        //if the makespan is improved then the new execution order is the best one
        //if the makespan is not improved then the initial execution order is the best one
        //print the best execution order

    }





}
