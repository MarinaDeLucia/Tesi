package it.delucia;

import it.delucia.algo.Greedy;
import it.delucia.exceptions.DuplicateResourceException;
import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;
import it.delucia.model.Resource;
import it.delucia.model.events.JobArrival;
import it.delucia.model.events.ResourceLoad;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ciao Marina!");
        System.out.println("Saluti da Roma");
        ModelLoader.getInstance().setMakespanThreshold(100);
        System.out.println("[DEBUG] THRESHOLD: " + ModelLoader.getInstance().getMakespanThreshold());

        //------------ LOADING RESOURCES ------------
        //create 3 resources
        Resource resource1 = new Resource(1, "Carbonio", 16);
        Resource resource2 = new Resource(2, "Platino", 100);
        Resource resource3 = new Resource(3, "Argento", 100);
        //adding them to the model
        try {
            ModelLoader.getInstance().addResource(resource1);
            ModelLoader.getInstance().addResource(resource2);
            ModelLoader.getInstance().addResource(resource3);
        } catch (DuplicateResourceException e) {
            System.out.println("[ERROR] Duplicate resource");
            System.out.println(e.getMessage());
        }

        //create 4 jobs
        Job job1 = new Job(1,
                    100,
                            new int[][]{{1, 2}, {2, 3}, {3, 3}, {4, 5}},
                            new int[][]{{1, 15}, {2, 5}, {3, 34}});
        Job job2 = new Job(2,
                    100,
                            new int[][]{{1, 4}, {2, 7}, {3, 4}, {4, 1}},
                            new int[][]{{1, 3}, {2, 12}, {3, 40}});
        Job job3 = new Job(3,
                    100,
                            new int[][]{{1, 1}, {2, 1}, {3, 3}, {4, 2}},
                            new int[][]{{1, 49}, {2, 20}, {3, 8}});

        ModelLoader.getInstance().addJob(job1);
        ModelLoader.getInstance().addJob(job2);
        ModelLoader.getInstance().addJob(job3);

        //adding a list of some resourceLoad events
        ModelLoader.getInstance().addResourceLoad(new ResourceLoad(resource1, 1, 3));
        ModelLoader.getInstance().addResourceLoad(new ResourceLoad(resource1, 10, 7));
        ModelLoader.getInstance().addResourceLoad(new ResourceLoad(resource1, 50, 9));

        //create job4
        Job job4 = new Job(4,
                    100,
                            new int[][]{{1, 1}, {2, 6}, {3, 3}, {4, 2}},
                            new int[][]{{1, 4}, {2, 7}, {3, 9}});
        //adding a job arrival at step 2
        ModelLoader.getInstance().addJobArrival(new JobArrival(job4, 2));



        System.out.println(" MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM 1    Model Jobs size: "+ModelLoader.getInstance().getJobs().size());

        //get sorted list of jobs
        List<Job> sortedJobs = ModelLoader.getInstance().getSortedJobs();

        //print sorted list of jobs
        for (Job job : sortedJobs) {
            System.out.println(job);
        }

        //print overall makespan
        int makespan = ModelLoader.getInstance().getMakespan();
        System.out.println("Overall makespan: " + makespan);

        // ----------------- GREEDY -----------------
        //init the greedy algorithm
        System.out.println(" MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM 2    Model Jobs size: "+ModelLoader.getInstance().getJobs().size());
        Greedy.getInstance().init(sortedJobs, 4);
        System.out.println(" MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM 3  Model Jobs size: "+ModelLoader.getInstance().getJobs().size());
        Pair<List<List<Job>>, Integer> solutions = Greedy.getInstance().run();
        System.out.println(" MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM  4   Model Jobs size: "+ModelLoader.getInstance().getJobs().size());


        Greedy.getInstance().execute(solutions.getLeft().get(0));


    }
}