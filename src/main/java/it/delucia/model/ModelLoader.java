package it.delucia.model;

import it.delucia.exceptions.DuplicateResourceException;
import it.delucia.model.events.JobArrival;
import it.delucia.model.events.ResourceLoad;

import java.util.*;

public class ModelLoader {
    private static ModelLoader instance = null;
    private List<Job> jobs = new ArrayList<>();
    private int makespanThreshold = 100;
    private List<Resource> resources = new LinkedList<>();
    private Map<Integer,Resource> idResourceMap = new HashMap<>();
    private Map<Integer,Integer>  resourceQUantityMap = new HashMap<>();
    private Map<Integer,List<ResourceLoad>> resourceLoadMap = new HashMap<>();
    private Map<Integer,List<JobArrival>>   jobArrivalMapByStep = new HashMap<>();

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

    public void addResourceLoad(ResourceLoad resourceLoad){
        if(resourceLoadMap.containsKey(resourceLoad.getResource().getId())){
            resourceLoadMap.get(resourceLoad.getResource().getId()).add(resourceLoad);
        }else{
            List<ResourceLoad> resourceLoads = new ArrayList<>();
            resourceLoads.add(resourceLoad);
            resourceLoadMap.put(resourceLoad.getResource().getId(),resourceLoads);
        }
    }

    public List<ResourceLoad> getResourceLoad(Resource resource){
        return resourceLoadMap.get(resource.getId());
    }

    public int getResourceQuantity(Resource resource){
        return resourceQUantityMap.get(resource.getId());
    }

    public Resource getResourceById(int id){
        return idResourceMap.get(id);
    }

    //get resource load by resource-id
    public List<ResourceLoad> getResourceLoad(int resourceId){
        return resourceLoadMap.get(resourceId);
    }

    public void addJobArrival(JobArrival jobArrival, int atStep){
        if(jobArrivalMapByStep.containsKey(atStep)){
            jobArrivalMapByStep.get(atStep).add(jobArrival);
        }else{
            List<JobArrival> jobArrivals = new ArrayList<>();
            jobArrivals.add(jobArrival);
            jobArrivalMapByStep.put(atStep,jobArrivals);
        }
    }

    public List<JobArrival> getJobArrivalByStep(int atStep){
        return jobArrivalMapByStep.get(atStep);
    }

    /**
     * return true when there is no more jobs in plan be processed
     * @return
     */
    public boolean hasNothingTodo() {
        return this.jobArrivalMapByStep.isEmpty() && this.jobs.isEmpty();
    }

    public void removeJob(Job job) {
        this.jobs.remove(job);
    }

    public void addJobs(List<Job> list) {
        this.jobs.addAll(list);
    }
}
