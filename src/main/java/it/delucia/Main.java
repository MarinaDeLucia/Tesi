package it.delucia;

import it.delucia.algo.Greedy;
import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ciao Marina!");
        System.out.println("Saluti da Roma");

        //create 4 jobs
        Job job1 = new Job(1, 100, new int[][]{{1,2},{2,3},{3,3},{4,5}});
        Job job2 = new Job(2, 100, new int[][]{{1,4},{2,7},{3,4},{4,1}});
        Job job3 = new Job(3, 100, new int[][]{{1,1},{2,1},{3,3},{4,2}});

        ModelLoader.getInstance().addJob(job1);
        ModelLoader.getInstance().addJob(job2);
        ModelLoader.getInstance().addJob(job3);

        //get sorted list of jobs
        List<Job> sortedJobs = ModelLoader.getInstance().getSortedJobs();

        //print sorted list of jobs
        for (Job job : sortedJobs) {
            System.out.println(job);
        }

        //print overall makespan
        int makespan = ModelLoader.getInstance().getMakespan();
        System.out.println("Overall makespan: " + makespan);

        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);


        System.out.println("init greedy");
        Greedy.getInstance().init(greedyJobs,4);
        System.out.println("run greedy");
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        System.out.println("Local makespan: " + localMakeSpan);


    }
}