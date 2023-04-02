package it.delucia.model.events;

import it.delucia.model.Job;

public class JobArrival extends Event {

    private Job job;

    public JobArrival(Job job, int step) {
        super(step);
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

}
