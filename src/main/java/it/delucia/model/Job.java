package it.delucia.model;

import java.util.Objects;

public class Job implements Comparable<Job> {
    private String id;
    private int dueDate;
    private int processingTime;

    public Job(String id, int dueDate, int processingTime) {
        this.id = id;
        this.dueDate = dueDate;
        this.processingTime = processingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    //return slack time
    public int getSlackTime() {
        return dueDate - this.processingTime;
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", dueDate=" + dueDate + ", processingTime=" + processingTime + '}';
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    //generate equal method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        return Objects.equals(this.id, other.id);
    }


    @Override
    public int compareTo(Job o) {
        //compare by slacktime
        return this.getSlackTime() - o.getSlackTime();
    }
}
