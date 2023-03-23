package it.delucia.algo;

import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedyTest {

    private static StringBuilder reportMessage = new StringBuilder();
    private String message;
    private String result;


    @AfterEach
    public void tearDown() throws Exception {
        reportMessage.append("\n"+message  + result);
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        System.out.println("\n\n===========================================================");
        System.out.println("                          TEST REPORT");
        System.out.print("===========================================================");
        System.out.println(reportMessage.toString());
        System.out.println("===========================================================\n\n");
    }


    @Test
    @DisplayName("[JUnit] Local Search (jobs=3, machines=4)")
    void localSearch3J4M(TestInfo info) {
        message = info.getDisplayName();
        Job job1 = new Job("A", 100, new int[][]{{1,2},{2,3},{3,3},{4,5}});
        Job job2 = new Job("B", 100, new int[][]{{1,4},{2,7},{3,4},{4,1}});
        Job job3 = new Job("C", 100, new int[][]{{1,1},{2,1},{3,3},{4,2}});
        ModelLoader.getInstance().setNumberOfMachines(4);

        List<Machine> machines = new LinkedList<>();
        machines.add(new Machine(1));
        machines.add(new Machine(2));
        machines.add(new Machine(3));
        machines.add(new Machine(4));

        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);
        Greedy.getInstance().init(greedyJobs, machines);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(22, localMakeSpan);
        result = "\t[PASSED]";
    }

    @Test
    @DisplayName("[JUnit] Local Search (jobs=4, machines=4)")
    void localSearch4J4M(TestInfo info) {
        message = info.getDisplayName();
        Job job1 = new Job("A", 100, new int[][]{{1,2},{2,3},{3,3},{4,5}});
        Job job2 = new Job("B", 100, new int[][]{{1,4},{2,7},{3,4},{4,1}});
        Job job3 = new Job("C", 100, new int[][]{{1,1},{2,1},{3,3},{4,2}});
        Job job4 = new Job("C", 100, new int[][]{{1,3},{2,6},{3,2},{4,2}});
        ModelLoader.getInstance().setNumberOfMachines(4);

        List<Machine> machines = new LinkedList<>();
        machines.add(new Machine(1));
        machines.add(new Machine(2));
        machines.add(new Machine(3));
        machines.add(new Machine(4));

        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);
        greedyJobs.add(job4);
        Greedy.getInstance().init(greedyJobs, machines);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(24, localMakeSpan);
        result = "\t[PASSED]";

    }
}