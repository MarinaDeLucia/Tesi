package it.delucia;

import it.delucia.model.Job;
import it.delucia.model.ModelLoader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Ciao Marina!");
        System.out.println("Saluti da Roma");

        //create 4 jobs
        Job job1 = new Job("A", 10, 5); //slack time = 5
        Job job2 = new Job("B", 35, 32); //slack time = 3
        Job job3 = new Job("C", 20, 2); //slack time = 18
        Job job4 = new Job("D", 25, 1); //slack time = 24

        ModelLoader.getInstance().addJob(job1);
        ModelLoader.getInstance().addJob(job2);
        ModelLoader.getInstance().addJob(job3);
        ModelLoader.getInstance().addJob(job4);

        //get sorted list of jobs
        List<Job> sortedJobs = ModelLoader.getInstance().getSortedJobs();

        //print sorted list of jobs
        for (Job job : sortedJobs) {
            System.out.println(job);
        }

        //print overall makespan
        int makespan = ModelLoader.getInstance().getMakespan();

        System.out.println("Overall makespan: " + makespan);

    }
}