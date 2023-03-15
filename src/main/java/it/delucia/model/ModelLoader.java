package it.delucia.model;

import java.util.ArrayList;
import java.util.List;

public class ModelLoader {
    private static ModelLoader instance = null;
    private List<Job> jobs = new ArrayList<>();
    private ModelLoader() {
    }

    public static ModelLoader getInstance() {
        if (instance == null) {
            instance = new ModelLoader();
        }
        return instance;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public List<Job> getJobs() {
        return jobs;
    }

    //returns sorted jobs list
    public List<Job> getSortedJobs() {
        jobs.sort((Job o1, Job o2) -> o1.getSlackTime() - o2.getSlackTime());
        return jobs;
    }

    //returns the overall makespan
    public int getMakespan() {
        int makespan = 0;
        for (Job job : jobs) {
            makespan += job.getProcessingTime();
        }
        return makespan;
    }
}
