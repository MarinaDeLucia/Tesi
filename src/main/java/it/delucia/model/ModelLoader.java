package it.delucia.model;

import it.delucia.exceptions.DuplicateResourceException;

import java.util.*;

public class ModelLoader {
    private static ModelLoader instance = null;
    private List<Job> jobs = new ArrayList<>();
    private int makespanThreshold = 100;
    private List<Resource> resources = new LinkedList<>();
    private Map<Integer,Resource> idResourceMap = new HashMap<>();
    private Map<Integer,Integer>  resourceQUantityMap = new HashMap<>();

    private int numberOfMachines = 0;

    private ModelLoader() {
    }

    public static ModelLoader getInstance() {
        if (instance == null) {
            instance = new ModelLoader();
        }
        return instance;
    }

    public int getMakespanThreshold() {
        return makespanThreshold;
    }

    public void addResource(Resource resource) throws DuplicateResourceException {
        if(resources.contains(resource)){
            throw new DuplicateResourceException(resource);
        }
        idResourceMap.put(resource.getId(), resource);
        resourceQUantityMap.put(resource.getId(),resource.getQuantity());
        resources.add(resource);


    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setMakespanThreshold(int makespanThreshold) {
        this.makespanThreshold = makespanThreshold;
    }

    public void clear() {
        if (jobs != null) {
            jobs.clear();
        }
        numberOfMachines = 0;
    }

    public void setNumberOfMachines(int numberOfMachines) {
        this.numberOfMachines = numberOfMachines;
    }

    public int getNumberOfMachines() {
        return numberOfMachines;
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
