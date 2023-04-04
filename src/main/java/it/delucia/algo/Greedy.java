package it.delucia.algo;

import it.delucia.Settings;
import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;
import it.delucia.model.Resource;
import it.delucia.model.events.JobArrival;
import it.delucia.model.events.ResourceLoad;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Greedy {
    //implement singleton pattern
    private static Greedy instance = null;
    private List<Job> jobs = null;
    private List<Job> extractedJobs = new LinkedList<>();
    private boolean initialized = false;
    private boolean fullExploration = false;

    private List<Job> solution = null;

    public static final int ENOUGH_RESOURCE_FOR_ALL_JOBS = -1;
    public static final int NOT_ENOUGH_RESOURCE_AT_ALL = -2;
    private boolean dirtyBacklog = false; //true if there is some new job to be processed

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

    public boolean isFullExploration() {
        return fullExploration;
    }

    public void setFullExploration(boolean fullExploration) {
        this.fullExploration = fullExploration;
    }

    public void clear() {
        if (jobs != null) {
            jobs.clear();
        }
        if (extractedJobs != null) {
            extractedJobs.clear();
        }
        initialized = false;
        if (solution != null) {
            solution.clear();
        }
        this.fullExploration = false;
    }

    //implement greedy algorithm
    public Pair<List<List<Job>>, Integer> run() {
        if (!initialized) {
            throw new RuntimeException("Greedy not initialized");
        }
        Pair<Job, Job> extractedJobs = destruction();
        //stampa i due job estratti dalla fase di distruzione:
        System.out.println("-------------------------- EXTRACTED JOBS FROM DESTRUCTION PHASE --------------------------");
        System.out.println(">> Greedy: Extracted Job1: " + extractedJobs.getLeft());
        System.out.println(">> Greedy: Extracted Job2: " + extractedJobs.getRight());
        System.out.println("------------------------------------------------------------------------------------------");


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

        List<List<Job>> allSolutions = new LinkedList<>();

        if (makespan_12 < makespan_21) {
            System.out.println(">> Local Search: Makespan: " + makespan_12);
            List<Job> tempSolution = new LinkedList<>();
            tempSolution.addAll(jobs_12);
            allSolutions.add(tempSolution);
        } else if(makespan_12 == makespan_21){
            //if makespan_12 == makespan_21 then add the jobs in the original order in the extracted jobs list
            System.out.println(">> Local Search:  Makespan(first one): " + makespan_12);
            System.out.println(">> Local Search: Makespan(second one): " + makespan_21);
            List<Job> tempSolution = new LinkedList<>();
            tempSolution.addAll(jobs_12);
            allSolutions.add(tempSolution);
            List<Job> tempSolution2 = new LinkedList<>();
            tempSolution2.addAll(jobs_21);
            allSolutions.add(tempSolution2);
        }else {
            //else add the jobs in the reversed order in the extracted jobs list
            System.out.println(">> Local Search: Makespan: " + makespan_21);
            List<Job> tempSolution = new LinkedList<>();
            tempSolution.addAll(jobs_21);
            allSolutions.add(tempSolution);
        }

        System.out.println("+++++++++++++++++++++++++++++++ STARTING GREEDY  +++++++++++++++++++++++++++++++");
        System.out.println(">> Initial minimal plans size: "+ allSolutions.size());

        //stampa la combinazione vincente della fase di distruzione
        for(List<Job> solution : allSolutions){
            System.out.println("-------------------------- WINNING ORDERED COMBINATION FROM DESTRUCTION PHASE --------------------------");
            System.out.println(">> Greedy: Extracted Job1: " + solution.get(0));
            System.out.println(">> Greedy: Extracted Job2: " + solution.get(1));
            System.out.println(">> Greedy: Makespan: " + Math.min(makespan_12, makespan_21));
            System.out.println("--------------------------------------------------------------------------------------------------------");
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        //ho estratto i primi due job e valutato l'ordinamento migliore in base al makespan
        //ora devo valutare l'ordinamento migliore tra i rimanenti job
        //ciclerò la lista jobs, che ora contiene tutti i job rimanenti, per ogni job
        //provo a inserirlo nella lista extractedJobs in tutte le posizioni possibili, e quindi valuto
        //il makespan. Alla fine del ciclo, il job che mi ha dato il makespan minore lo inserisco nella
        //lista extractedJobs nella posizione che mi ha dato il makespan minore
        //per ogni job rimanente
        int currentBestMakespan = 0;

        Map<Integer, List<List<Job>>> partialResults = new HashMap<>();

        for (Job job : jobs) {
            List<List<Job>> newSolutions = new LinkedList<>();
            Iterator<List<Job>> iterator = allSolutions.iterator();
            while(iterator.hasNext()){
                System.out.println(">>>>>>>>>>>>> INSERTING JOB: " + job.printId() + " <<<<<<<<<<<<<<<<");
                //find the best order for the current job
                Pair<List<List<Job>>, Integer> allMinimalSequences = findNewBestOrder(iterator.next(), job);
                //estraggo tutte le sequenze minimali
                List<List<Job>> minimalSequences = allMinimalSequences.getLeft();
                int minimalMakespan = allMinimalSequences.getRight();
                System.out.println(">> Greedy: Minimal Makespan: " + minimalMakespan);
                //aggiorno il makespan corrente
                if(partialResults.containsKey(minimalMakespan)){
                    //se il makespan corrente è già presente, aggiungo le nuove sequenze
                    partialResults.get(minimalMakespan).addAll(minimalSequences);
                }else{
                    //se il makespan corrente non è presente, aggiungo il nuovo makespan e le nuove sequenze
                    partialResults.put(minimalMakespan, minimalSequences);
                }
                newSolutions.addAll(minimalSequences);
                if(!iterator.hasNext()){
                    int minMakespan = Collections.min(partialResults.keySet());
                    currentBestMakespan = minMakespan;

                    allSolutions = newSolutions;
                    partialResults.clear();
                }
            }
            //esamino la mappa partialResults e prendo tutte le sequenze associato al makespan minore. Svuoto quindi
            //la lista allSolutions e aggiungo le nuove sequenze. Infine svuoto la mappa partialResults.


        }


        //stampa la combinazione vincente della fase di costruzione
        System.out.println("\n\n++++++++++++++++++++++++++++ FINAL SOLUTIONS ++++++++++++++++++++++++++++\n");
        if(!isFeasible(currentBestMakespan)){
            System.out.println("\n**********************          WARNING         **********************\n");
            System.out.println("                             PLAN NOT FEASIBLE\n");
            System.out.println(">> The Solution Plan is NOT FEASIBLE because the makespan is greater than the threshold:"+ ModelLoader.getInstance().getMakespanThreshold());
            System.out.println(">> amount of time needed to complete all jobs: " + (currentBestMakespan - ModelLoader.getInstance().getMakespanThreshold()));
            System.out.println(("************************************************************************"));
        }
        int i=1;
        for(List<Job> solution : allSolutions) {
            System.out.println(">> PLAN "+ i + ")");
            String feasible = isFeasible(currentBestMakespan) ? "FEASIBLE" : "NOT FEASIBLE";
            System.out.println(">> Greedy Makespan: " + currentBestMakespan);
            System.out.println(">> Makspan Trheshold: " + ModelLoader.getInstance().getMakespanThreshold());
            System.out.println(">> The Solution Plan is: " + feasible);

            //print all jobs of this plan in one line, use java stream and use "," as separator
            System.out.println(solution.stream().map(Job::printId).collect(Collectors.joining(", ")));
            i++;
            System.out.println("------------------------------------------------------------------------");
        }

        //fix the jobs in ModelLoaer:
        ModelLoader.getInstance().getJobs().clear();
        for(Job job : allSolutions.get(0)){
            ModelLoader.getInstance().getJobs().add(job);
        }

        //return all the minimal solutions according to the method signature
        return Pair.of(allSolutions, currentBestMakespan);
    }

    private boolean isFeasible(int makespan) {
        int makespanThreshold = ModelLoader.getInstance().getMakespanThreshold();
        return makespan <= makespanThreshold;
    }

    //print all jobs
    public void printJobs() {
        if (!initialized) {
            throw new RuntimeException("Greedy not initialized");
        }
        for (Job job : jobs) {
            System.out.println("Greedy: job -> " + job);
        }
    }

    private Pair<Job, Job> destruction() {
        System.out.println("Greedy: destruction");
        System.out.println("Initial jobs list");
        printJobs();
        System.out.println("--------------------------- BEFORE DESTRUCTION --------------------------------");
        System.out.print("Job\t");
        for (int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
            System.out.print("M" + i + "\t");
        }
        System.out.println();
        //print the jobs
        for (Job job : jobs) {
            System.out.print(job.getId() + "\t");
            for (int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
                System.out.print(job.getProcessingTime(i) + "\t");
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------");

        //if size of the list is <= 2 then throw an exception
        if (jobs.size() <= 2) {
            throw new RuntimeException("Greedy: jobs list size is <= 2");
        }
        //extract the first two jobs
        Job job_extracted_1 = this.jobs.remove(0);
        Job job_extracted_2 = this.jobs.remove(0);

        //print the extracted jobs
        System.out.println("Greedy: extracted jobs -> " + job_extracted_1 + " " + job_extracted_2);

        //print the remaining jobs
        System.out.println("Greedy: remaining jobs");
        //create a pair with the extracted jobs and return it
        return Pair.of(job_extracted_1, job_extracted_2);
    }

    public int localSearch(List<Job> jobs) {
        System.out.println("Greedy: local search");
        System.out.println("print job time on machines");
        //print a table where each row is a job and each column is a machine, print also the header
        //print the header
        System.out.print("Job\t");
        for (int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
            System.out.print("M" + i + "\t");
        }
        System.out.println();
        //print the jobs
        for (Job job : jobs) {
            System.out.print(job.getId() + "\t");
            for (int i = 1; i <= ModelLoader.getInstance().getNumberOfMachines(); i++) {
                System.out.print(job.getProcessingTime(i) + "\t");
            }
            System.out.println();
        }


        //evaluate which is the best execution order of the remaining jobs. The evaluation is based on the makespan
        if (jobs.size() < 2) {
            throw new RuntimeException("Greedy: jobs list size is <= 2");
        } else {
            int numberOfJobs = jobs.size();
            int numberOfMachines = ModelLoader.getInstance().getNumberOfMachines();
            System.out.println("Greedy: number of jobs: " + numberOfJobs);
            System.out.println("Greedy: number of machines: " + numberOfMachines);
            //create a matrix of size numberOfJobs x numberOfMachines
            int[][] matrix = new int[numberOfJobs][numberOfMachines];
            //initialize the matrix
            for (int i = 0; i < numberOfJobs; i++) {
                for (int j = 0; j < numberOfMachines; j++) {
                    matrix[i][j] = 0;
                }
            }
            //complete the first row with all the processing times of the jobs, the i cell contains the sum of the previous processing time plus the current one
            for (int i = 0; i < numberOfMachines; i++) {
                if (i == 0) {
                    matrix[0][i] = jobs.get(0).getProcessingTime(1);
                } else {
                    matrix[0][i] = matrix[0][i - 1] + jobs.get(0).getProcessingTime(1 + i);
                }
            }
            //complete the first column with all processing time of the all jobs at machine 1
            for (int i = 1; i < numberOfJobs; i++) {
                matrix[i][0] = matrix[i - 1][0] + jobs.get(i).getProcessingTime(1);
            }


            //complete the matrix
            for (int i = 1; i < numberOfJobs; i++) {
                for (int j = 1; j < numberOfMachines; j++) {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]) + jobs.get(i).getProcessingTime(j + 1);
                }
            }

            //print the matrix for debug
            for (int i = 0; i < numberOfJobs; i++) {
                for (int j = 0; j < numberOfMachines; j++) {
                    System.out.print(" " + matrix[i][j]);
                }
                System.out.println(" ");
            }

            //return the makespan
            return matrix[numberOfJobs - 1][numberOfMachines - 1];


        }
        //if the makespan is improved then the new execution order is the best one
        //if the makespan is not improved then the initial execution order is the best one
        //print the best execution order

    }


    public int[][] generateAllPossibleOrder(List<Job> sequence) {
        //complete an integer array with all id of the jobs
        int[] jobsId = new int[sequence.size()];
        for (int i = 0; i < sequence.size(); i++) {
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
     *
     * @param immutableSeq
     * @param newElement
     * @return
     */
    public Pair<List<List<Job>>, Integer> findNewBestOrder(List<Job> immutableSeq, Job newElement) {
        System.out.println("Greedy: find new best order");
        //create a list of jobs that contains the immutableSeq and the newElement in the head
        List<List<Job>> allEqualMinimalSequences = new LinkedList<>();
        List<Job> result = new LinkedList<>();
        result.addAll(immutableSeq);
        result.add(0, newElement);

        int bestMakespan = localSearch(result);
        System.out.println("Greedy: local makespan: " + bestMakespan);

        Map<Integer,List<List<Job>>> map = new HashMap<>(); //map of makespan and sequence

        List<List<Job>> list = new LinkedList<>();
        list.add(result);
        map.put(bestMakespan,list);


        //create a cycle where each step move the head one step to the right
        for (int i = 1; i < result.size(); i++) {
            //create a new list of jobs
            List<Job> newSeq = new LinkedList<>();
            //add the rest of the sequence
            newSeq.addAll(immutableSeq);
            //move the head to the right
            newSeq.add(i, newElement);
            //print the new sequence
            System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> Greedy: new sequence");
            for (Job job : newSeq) {
                System.out.print(job.getId() + " ");
            }
            System.out.println("\n--------------------------------------");
            //evaluate the makespan of the new sequence
            int makespan = localSearch(newSeq);
            System.out.println("Greedy: local makespan: " + makespan);
            //if the makespan is better than the result is the newSeq
            if (makespan < bestMakespan) {
                bestMakespan = makespan;
                result = newSeq;
            }
            if(map.containsKey(makespan)){
                List<List<Job>> tlist = map.get(makespan);
                tlist.add(newSeq);
                map.put(makespan,tlist);
            }else{
                List<List<Job>> tlist = new LinkedList<>();
                tlist.add(newSeq);
                map.put(makespan,tlist);
            }
        }
        //return the best sequence
        System.out.println("Greedy: best makespan: " + bestMakespan);
        Pair<List<List<Job>>, Integer> pair = Pair.of(map.get(bestMakespan), bestMakespan);
        System.out.println(">>>>> Ho trovato " + map.get(bestMakespan).size() + " sequenze con makespan " + bestMakespan);
        return pair;
    }


    public void execute(List<Job> jobs) {
        this.jobs = jobs;
        System.out.println(" ==============================  E X E C U T I O N ============================== ");
        //print all the resources with their quantity.
        System.out.println("Jobs: ");
        for (Job job : jobs) {
            System.out.println("[JOB] "+job.getId()+" with processing time: " + job.getProcessingTime(1) + " " + job.getProcessingTime(2) + " " + job.getProcessingTime(3));
        }
        System.out.println("--------------------------------------");
        System.out.println("Resources: ");
        for (Resource resource : ModelLoader.getInstance().getResources()) {
            System.out.println("[RESOURCE] {"+resource.getName()+"}\t with id "+resource.getId() + " and quantity: " + resource.getQuantity());
        }
        System.out.println("--------------------------------------");


        System.out.println("Check before execution: ");
        //print size of jobs of modelloader
        System.out.println("Jobs in modelLoader: " + ModelLoader.getInstance().getJobs().size());
        //print all jobs
        int step = 1;
//        List<Job> backlog = new LinkedList<>(); //list of jobs that cant be done because there are not enough resources
        while(!ModelLoader.getInstance().hasNothingTodo() && step < Settings.MAX_STEP){
            System.out.println("**********************************************************");
            System.out.println("Step " + step + ": ");
            System.out.println("**********************************************************");

            if(isDirtyBacklog()){
                System.out.println(" -------------------- DIRTY BACKLOG -------------------- ");
                List<JobArrival> jobArrivalByStep = ModelLoader.getInstance().getJobArrivalByStep(step-1);
                if(jobArrivalByStep!=null){
                    System.out.println(" << im going to process a job arrival:");
                    //jobs.addAll(jobArrivalByStep.stream().map(JobArrival::getJob).toList());
                    //add all new jobs to modelLoader
                    System.out.println("BEFORE - WARNING -> model loader jobs size: "+ModelLoader.getInstance().getJobs().size());
                    ModelLoader.getInstance().addJobs(jobArrivalByStep.stream().map(JobArrival::getJob).toList());
                    System.out.println("AFTER - WARNING -> model loader jobs size: "+ModelLoader.getInstance().getJobs().size());

                    // ----------------- GREEDY -----------------
                    //init the greedy algorithm
                    this.jobs = ModelLoader.getInstance().getSortedJobs();
                    Pair<List<List<Job>>, Integer> solutions = Greedy.getInstance().run();
                    //extract the new order by the solution
                    this.jobs = solutions.getLeft().get(0);
                    jobs = this.jobs;

                    System.out.println(" -------------------- CLEAN BACKLOG -------------------- ");
                    ModelLoader.getInstance().clearJobArrivalStep(step-1);
                    dirtyBacklog = false;

                }
            }

            //check if there are enough resource to complete the first job
            int result = analyzeJobsByResources(jobs);
            if(result != NOT_ENOUGH_RESOURCE_AT_ALL){
                //the result represent the id of the first job that has to go to the backlog
                process(jobs.get(0));
            }else{
                System.out.println("There are not enough resources to complete the next job");
            }
            //check if there is some new load of resource at this step and update the resources amount
            for(Resource resource : ModelLoader.getInstance().getResources()){
                //find resource load by resource id, and then filter it by step by using java stream
                final int s = step;
                List<ResourceLoad> resourceLoad = ModelLoader.getInstance().getResourceLoad(resource);
                if (resourceLoad == null) {
                    continue;
                }
                for(ResourceLoad rl : resourceLoad){
                    System.out.println("ResourceLoad: " + rl.getResource().getName() + " at step " + rl.getStep() + " has the quantity " + rl.getQuantity());
                }
                int quantityAtThisStep = 0;
                for(ResourceLoad rl : resourceLoad){
                    if(rl.getStep() == step){
                        quantityAtThisStep += rl.getQuantity();
                        //ModelLoader.getInstance().depleteResourceLoad(rl);
                    }
                }

                System.out.println(">> Total quantity to add to resource " + resource.getName() + " is " + quantityAtThisStep + " at step " + step + "");
                resource.addQuantity(quantityAtThisStep);
            }

            //check if there is some new JobArrival in model for this step. If so, get those and move them into the
            //job list and set the dirtyjob flag to true
            List<JobArrival> jobArrivalByStep = ModelLoader.getInstance().getJobArrivalByStep(step);
            if(jobArrivalByStep!= null && !jobArrivalByStep.isEmpty()){
                System.out.println("WARNING, at the step "+step+" there is a Job Arrival !");
                this.dirtyBacklog = true;
            }

            step++;
        }
        System.out.println("Finished at step: " + step);
        System.out.println(ModelLoader.getInstance().getJobs().size());
        System.out.println("-----------------------------------");
    }

    public boolean isDirtyBacklog(){
        return this.dirtyBacklog;
    }


    public void process(Job job){
        System.out.println("Process job " + job.getId());
        //update the resources quantity
        for(Resource resource : ModelLoader.getInstance().getResources()){
            Integer consumptionByResource = job.getConsumptionByResource(resource.getId());
            System.out.println("Job: "+job.getId()+" consume "+consumptionByResource+" of resource "+resource.getName());
            resource.removeQuantity(consumptionByResource);
        }
        //remove job from the jobs list
        ModelLoader.getInstance().removeJob(job);
        this.jobs.remove(job);
    }

    public int analyzeJobsByResources(List<Job> jobs){
        //check if there are enough resources to execute the jobs
        //list all resources available and for each resource check if there are enough resources to complete the job
        //clone the resource quantity map and for each job subtract the processing time of the job from the resource quantity
        Map<Integer, Integer> resourcesQuantity = new HashMap<>();
        for(Resource resource : ModelLoader.getInstance().getResources()){
            resourcesQuantity.put(resource.getId(), resource.getQuantity());
        }
        for(Resource resource : ModelLoader.getInstance().getResources()){
            for(Job job : jobs){
                int quantity = resourcesQuantity.get(resource.getId());
                quantity -= job.getConsumptionByResource(resource.getId());
                resourcesQuantity.put(resource.getId(), quantity);
                if(quantity < 0){
                    return job.getId() == jobs.get(0).getId() ? NOT_ENOUGH_RESOURCE_AT_ALL : job.getId();
                }
            }
        }
        return ENOUGH_RESOURCE_FOR_ALL_JOBS;
    }



    public static void main(String[] args) {
        //test the method findNewBestOrder

        List<Job> jobs = new LinkedList<>();
        ModelLoader.getInstance().setNumberOfMachines(3);

        Job job1 = new Job(1, 100, new int[][]{{1, 10}, {2, 12}, {3, 3}});
        Job job2 = new Job(2, 100, new int[][]{{1,  5}, {2,  1}, {3, 6}});
        Job job3 = new Job(3, 100, new int[][]{{1,  1}, {2, 11}, {3, 6}});
        Job job4 = new Job(4, 100, new int[][]{{1,  4}, {2,  4}, {3, 7}});
        Job job5 = new Job(5, 100, new int[][]{{1, 10}, {2,  5}, {3, 3}});

        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);
        jobs.add(job4);
        //create a list of 3 machines
        List<Machine> machines = new LinkedList<>();
        Machine machine1 = new Machine(1);
        Machine machine2 = new Machine(2);
        Machine machine3 = new Machine(3);
        Greedy.getInstance().init(jobs, 4);

        List<List<Job>> result = Greedy.getInstance().findNewBestOrder(jobs, job5).getLeft();

        //print results numbering the sequence
        int i = 1;
        for (List<Job> sequence : result) {
            System.out.println("Sequence " + i);
            for (Job job : sequence) {
                System.out.print(job.getId() + " ");
            }
            System.out.println(" ----------------------------------------");
            i++;
        }
 

    }
}
