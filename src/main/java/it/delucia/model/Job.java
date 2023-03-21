package it.delucia.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Job implements Comparable<Job> {
    private String id;
    private int dueDate;
    private Map<Integer, Integer> processingTimesMap; //key: machine id, value: processing time
    private int slackTime = -1; //slack time of the job
    private int processingTime = -1; //processing time of the job

    public Job(String id, int dueDate, Map<Integer, Integer> processingTimesMap) {
        this.id = id;
        this.dueDate = dueDate;
        this.processingTimesMap = processingTimesMap;
    }

    public Job(String id, int dueDate, int [][] processingMatrix) {
        this.id = id;
        this.dueDate = dueDate;
        processingMatrixToMap(processingMatrix);
    }

    private void processingMatrixToMap(int [][] processingMatrix){
        processingTimesMap = new java.util.HashMap<>();
        for (int i = 0; i < processingMatrix.length; i++) {
            processingTimesMap.put(processingMatrix[i][0], processingMatrix[i][1]);
        }
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
        if (processingTime == -1) {
            processingTime = 0;
            for (Map.Entry<Integer, Integer> entry : processingTimesMap.entrySet()) {
                processingTime += entry.getValue();
            }
        }
        return processingTime;
    }


    //returns the processing time of the job on the given machine
    public int getProcessingTime(int machineId) {
        return processingTimesMap.get(machineId);
    }

    //return slack time
    public int getSlackTime() {
        if (slackTime == -1) {
            slackTime = dueDate - getProcessingTime();
        }
        return slackTime;
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
        return  o.getSlackTime() -this.getSlackTime() ;
    }
}
