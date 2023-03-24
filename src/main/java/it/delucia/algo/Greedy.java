package it.delucia.algo;

import it.delucia.Settings;
import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
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
        Pair<Job, Job> extractedJobs = destruction();
        //create a list of jobs that contains the extracted jobs
        List<Job> jobs = new LinkedList<>();
        jobs.add(extractedJobs.getLeft());
        jobs.add(extractedJobs.getRight());
        int makespan = localSearch(jobs);
        System.out.println(">> Local Search: Makespan: " + makespan);
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

    private Pair<Job,Job> destruction(){
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
        //create a pair with the extracted jobs and return it
        return Pair.of(job_extracted_1, job_extracted_2);
    }

    public int localSearch(List<Job> jobs){
        System.out.println("Greedy: local search");
        System.out.println("print job time on machines");
        //print a table where each row is a job and each column is a machine, print also the header
        //print the header
        System.out.print("Job\t");
        for(Machine machine: machines) {
            System.out.print(machine.getId()+"\t");
        }
        System.out.println();
        //print the jobs
        for(Job job: jobs) {
            System.out.print(job.getId()+"\t");
            for(Machine machine: machines) {
                System.out.print(job.getProcessingTime(machine.getId())+"\t");
            }
            System.out.println();
        }


        //evaluate which is the best execution order of the remaining jobs. The evaluation is based on the makespan
        if(jobs.size() < 2) {
            throw new RuntimeException("Greedy: jobs list size is <= 2");
        }else {
            int numberOfJobs = jobs.size();
            int numberOfMachines = ModelLoader.getInstance().getNumberOfMachines();
            System.out.println("Greedy: number of jobs: "+numberOfJobs);
            System.out.println("Greedy: number of machines: "+numberOfMachines);
            //create a matrix of size numberOfJobs x numberOfMachines
            int[][] matrix = new int[numberOfJobs][numberOfMachines];
            //initialize the matrix
            for(int i = 0; i < numberOfJobs; i++) {
                for(int j = 0; j < numberOfMachines; j++) {
                    matrix[i][j] = 0;
                }
            }
            //complete the first row with all the processing times of the jobs, the i cell contains the sum of the previous processing time plus the current one
            for(int i = 0; i < numberOfMachines; i++) {
                if(i == 0) {
                    matrix[0][i] = jobs.get(0).getProcessingTime(1);
                }else {
                    matrix[0][i] = matrix[0][i-1] + jobs.get(0).getProcessingTime(1+i);
                }
            }
            //complete the first column with all processing time of the all jobs at machine 1
            for(int i = 1; i < numberOfJobs; i++) {
                    matrix[i][0] = matrix[i-1][0] + jobs.get(i).getProcessingTime(1);
            }


            //complete the matrix
            for(int i = 1; i < numberOfJobs; i++) {
                for(int j = 1; j < numberOfMachines; j++) {
                    matrix[i][j] = Math.max(matrix[i-1][j], matrix[i][j-1]) + jobs.get(i).getProcessingTime(j+1);
                }
            }

            //print the matrix for debug
            for(int i = 0; i < numberOfJobs; i++) {
                for(int j = 0; j < numberOfMachines; j++) {
                    System.out.print(" "+matrix[i][j]);
                }
                System.out.println(" ");
            }

            //return the makespan
            return matrix[numberOfJobs-1][numberOfMachines-1];


        }
        //if the makespan is improved then the new execution order is the best one
        //if the makespan is not improved then the initial execution order is the best one
        //print the best execution order

    }


    public int[][] generateAllPossibleOrder(List<Job> sequence){
        //complete an integer array with all id of the jobs
        int[] jobsId = new int[sequence.size()];
        for(int i = 0; i < sequence.size(); i++) {
            jobsId[i] = sequence.get(i).getId();
        }
        int maxOrders = Utils.factorial(sequence.size());
        int[][] allOrders = new int[maxOrders][sequence.size()];
        //generate all possible orders
        allOrders = Utils.generateAllPossibleOrder(jobsId);
        return allOrders;

    }


}
