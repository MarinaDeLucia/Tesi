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
    private List<Job> extractedJobs = new LinkedList<>();
    private boolean initialized = false;

    private List<Job> solution = null;

    private Greedy() {
    }

    public static Greedy getInstance() {
        if (instance == null) {
            instance = new Greedy();
        }
        return instance;
    }

    public void init(List<Job> jobs, int numberOfMachines) {
        this.jobs = jobs;
        System.out.println(">> Greedy: Number of jobs: " + jobs.size());
        initialized = true;
        ModelLoader.getInstance().setNumberOfMachines(numberOfMachines);
    }

    //implement greedy algorithm
    public Pair<List<Job>,Integer> run() {
        if(!initialized) {
            throw new RuntimeException("Greedy not initialized");
        }
        Pair<Job, Job> extractedJobs = destruction();
        //create a list of jobs that contains the extracted jobs
        List<Job> jobs_12 = new LinkedList<>();
        jobs_12.add(extractedJobs.getLeft());
        jobs_12.add(extractedJobs.getRight());
        int makespan_12 = localSearch(jobs_12);
        //create a new list with the reverdesd order of the extracted jobs
        List<Job> jobs_21 = new LinkedList<>();
        jobs_21.add(extractedJobs.getRight());
        jobs_21.add(extractedJobs.getLeft());
        int makespan_21 = localSearch(jobs_21);
        //if makespan_12 < makespan_21 then add the jobs in the original order in the extracted jobs list
        if(makespan_12 < makespan_21) {
            this.extractedJobs.add(extractedJobs.getLeft());
            this.extractedJobs.add(extractedJobs.getRight());
            System.out.println(">> Local Search: Makespan: " + makespan_12);
        } else {
            //else add the jobs in the reversed order in the extracted jobs list
            this.extractedJobs.add(extractedJobs.getRight());
            this.extractedJobs.add(extractedJobs.getLeft());
            System.out.println(">> Local Search: Makespan: " + makespan_21);
        }
        //ho estratto i primi due job e valutato l'ordinamento migliore in base al makespan
        //ora devo valutare l'ordinamento migliore tra i rimanenti job
        //ciclerò la lista jobs, che ora contiene tutti i job rimanenti, per ogni job
        //provo a inserirlo nella lista extractedJobs in tutte le posizioni possibili, e quindi valuto
        //il makespan. Alla fine del ciclo, il job che mi ha dato il makespan minore lo inserisco nella
        //lista extractedJobs nella posizione che mi ha dato il makespan minore
        //per ogni job rimanente
        int currentBestMakespan = 0;
        for(Job job: jobs) {
            Pair<List<Job>, Integer> newBestOrder = findNewBestOrder(this.extractedJobs, job);
            this.extractedJobs = newBestOrder.getLeft();
            currentBestMakespan = newBestOrder.getRight();
        }
        this.solution = new LinkedList<>(this.extractedJobs);
        //print the solution
        System.out.println("Greedy: solution");
        for(Job job: this.solution) {
            System.out.println(job);
        }

        Pair<List<Job>,Integer> solution = Pair.of(this.solution, currentBestMakespan);
        return solution;

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
        for(int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
            System.out.print("M"+i+"\t");
        }
        System.out.println();
        //print the jobs
        for(Job job: jobs) {
            System.out.print(job.getId()+"\t");
            for(int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
                System.out.print(job.getProcessingTime(i)+"\t");
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

    /**
     * This method explore all the possible sequences generated by mixing the immutableSeq
     * with the new element. In the creation of the all possible sequence, the order
     * of the elements of the ImmutableSeq must be kept. The new element can be placed
     * in between, before o after the elements of the sequence but the main order will
     * be kept.
     * @param immutableSeq
     * @param newElement
     * @return
     */
    public Pair<List<Job>,Integer> findNewBestOrder(List<Job> immutableSeq, Job newElement){
        System.out.println("Greedy: find new best order");
        //create a list of jobs that contains the immutableSeq and the newElement in the head
        List<Job> result = new LinkedList<>();
        result.addAll(immutableSeq);
        result.add(0, newElement);

        int bestMakespan = localSearch(result);

        //create a cycle where each step move the head one step to the right
        for(int i = 1; i < result.size(); i++) {
            //create a new list of jobs
            List<Job> newSeq = new LinkedList<>();
            //add the rest of the sequence
            newSeq.addAll(immutableSeq);
            //move the head to the right
            newSeq.add(i, newElement);
            //print the new sequence
            System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> Greedy: new sequence");
            for(Job job: newSeq) {
                System.out.print(job.getId()+" ");
            }
            System.out.println("\n--------------------------------------");
            //evaluate the makespan of the new sequence
            int makespan = localSearch(newSeq);
            //if the makespan is better then the result is the newSeq
            if(makespan < bestMakespan){
                bestMakespan = makespan;
                result = newSeq;
            }

        }
        //return the best sequence
        System.out.println("Greedy: best makespan: "+bestMakespan);
        Pair<List<Job>,Integer> pair = Pair.of(result, bestMakespan);
        return pair;
    }


    public static void main(String[] args) {
        //test the method findNewBestOrder

        List<Job> jobs = new LinkedList<>();
        ModelLoader.getInstance().setNumberOfMachines(3);



        Job job1 = new Job(1,100,new int[][]{{1,10},{2,12},{3,3}});
        Job job2 = new Job(2,100,new int[][]{{1,5},{2,1},{3,6}});
        Job job3 = new Job(3,100,new int[][]{{1,1},{2,11},{3,6}});
        Job job4 = new Job(4,100,new int[][]{{1,4},{2,4},{3,7}});
        Job job5 = new Job(5,100,new int[][]{{1,10},{2,5},{3,3}});

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        //create a list of 3 machines
        List<Machine> machines = new LinkedList<>();
        Machine machine1 = new Machine(1);
        Machine machine2 = new Machine(2);
        Machine machine3 = new Machine(3);
        Greedy.getInstance().init(jobs,4);

        List<Job> result = Greedy.getInstance().findNewBestOrder(jobs, job5).getLeft();

        System.out.println("Greedy: result");
        for(Job job: result) {
            System.out.print(job.getId()+" ");
        }
        System.out.println();

    }
}
