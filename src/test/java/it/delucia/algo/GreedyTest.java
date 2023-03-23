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

    @Test
    @DisplayName("[JUnit] Local Search (jobs=10, machines=10)")
    void localSearch10J10M(TestInfo info) {
        message = info.getDisplayName();
        Job job1 = new Job("A", 100, new int[][]{{1,1},{2,2},{3,5},{4,2},{5,1},{6,5},{7,1},{8,2},{9,5},{10,2}});
        //3 6 9 12 9 6 3 6 9 12
        //1 3 5 7 9 11 1 5 3 9
        //2 2 1 1 1 3 6 7 9 12
        //5 10 15 12 11 7 3 10 1 9
        //1 1 1 1 1 1 1 1 1 1
        //7 3 3 3 7 1 10 11 12 1
        //2 5 2 5 1 2 6 1 3 8
        //1 3 5 4 2 1 3 1 2 9
        //1 6 5 3 1 3 1 2 8 7
        //ora completa gli altri 9 job mancanti secondo i commenti precedenti dove ogni riga rappresenta il secondo parametro di ogni macchina
        Job job2 = new Job("B", 100, new int[][]{{1,3},{2,6},{3,9},{4,12},{5,9},{6,6},{7,3},{8,6},{9,9},{10,12}});
        Job job3 = new Job("C", 100, new int[][]{{1,1},{2,3},{3,5},{4,7},{5,9},{6,11},{7,1},{8,5},{9,3},{10,9}});
        Job job4 = new Job("D", 100, new int[][]{{1,2},{2,2},{3,1},{4,1},{5,1},{6,3},{7,6},{8,7},{9,9},{10,12}});
        Job job5 = new Job("E", 100, new int[][]{{1,5},{2,10},{3,15},{4,12},{5,11},{6,7},{7,3},{8,10},{9,1},{10,9}});
        Job job6 = new Job("F", 100, new int[][]{{1,1},{2,1},{3,1},{4,1},{5,1},{6,1},{7,1},{8,1},{9,1},{10,1}});
        Job job7 = new Job("G", 100, new int[][]{{1,7},{2,3},{3,3},{4,3},{5,7},{6,1},{7,10},{8,11},{9,12},{10,1}});
        Job job8 = new Job("H", 100, new int[][]{{1,2},{2,5},{3,2},{4,5},{5,1},{6,2},{7,6},{8,1},{9,3},{10,8}});
        Job job9 = new Job("I", 100, new int[][]{{1,1},{2,3},{3,5},{4,4},{5,2},{6,1},{7,3},{8,1},{9,2},{10,9}});
        Job job10 = new Job("L", 100, new int[][]{{1,1},{2,6},{3,5},{4,3},{5,1},{6,3},{7,1},{8,2},{9,8},{10,7}});
        ModelLoader.getInstance().setNumberOfMachines(10);

        List<Machine> machines = new LinkedList<>();
        for (int i = 1; i <= 10; i++) {
            machines.add(new Machine(i));
        }

        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);
        greedyJobs.add(job4);
        greedyJobs.add(job5);
        greedyJobs.add(job6);
        greedyJobs.add(job7);
        greedyJobs.add(job8);
        greedyJobs.add(job9);
        greedyJobs.add(job10);

        Greedy.getInstance().init(greedyJobs, machines);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(137, localMakeSpan);
        result = "\t[PASSED]";

    }
}