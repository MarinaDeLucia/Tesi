package it.delucia.algo;

import it.delucia.model.Job;
import it.delucia.model.Machine;
import it.delucia.model.ModelLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GreedyTest {

    private static StringBuilder reportMessage = new StringBuilder();
    private String message = "";
    private String result = "";


    @AfterEach
    public void tearDown() throws Exception {
        reportMessage.append("\n" + message + "\t"+result);
        ModelLoader.getInstance().clear();
        Greedy.getInstance().clear();
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
        Greedy.getInstance().setFullExploration(false);
        Job job1 = new Job(1, 100, new int[][]{{1, 2}, {2, 3}, {3, 3}, {4, 5}});
        Job job2 = new Job(2, 100, new int[][]{{1, 4}, {2, 7}, {3, 4}, {4, 1}});
        Job job3 = new Job(3, 100, new int[][]{{1, 1}, {2, 1}, {3, 3}, {4, 2}});

        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);
        Greedy.getInstance().init(greedyJobs, 4);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(22, localMakeSpan);
        result = "\t[PASSED]";
    }

    @Test
    @DisplayName("[JUnit] Local Search (jobs=4, machines=4)")
    void localSearch4J4M(TestInfo info) {
        message = info.getDisplayName();
        Greedy.getInstance().setFullExploration(false);
        Job job1 = new Job(1, 100, new int[][]{{1, 2}, {2, 3}, {3, 3}, {4, 5}});
        Job job2 = new Job(2, 100, new int[][]{{1, 4}, {2, 7}, {3, 4}, {4, 1}});
        Job job3 = new Job(3, 100, new int[][]{{1, 1}, {2, 1}, {3, 3}, {4, 2}});
        Job job4 = new Job(4, 100, new int[][]{{1, 3}, {2, 6}, {3, 2}, {4, 2}});


        List<Job> greedyJobs = new LinkedList<>();
        greedyJobs.add(job1);
        greedyJobs.add(job2);
        greedyJobs.add(job3);
        greedyJobs.add(job4);
        Greedy.getInstance().init(greedyJobs, 4);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(24, localMakeSpan);
        result = "\t[PASSED]";

    }

    @Test
    @DisplayName("[JUnit] Local Search (jobs=10, machines=10)")
    void localSearch10J10M(TestInfo info) {
        message = info.getDisplayName();
        Greedy.getInstance().setFullExploration(false);
        Job job1 = new Job(1, 100, new int[][]{{1, 1}, {2, 2}, {3, 5}, {4, 2}, {5, 1}, {6, 5}, {7, 1}, {8, 2}, {9, 5}, {10, 2}});
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
        Job job2 = new Job(2, 100, new int[][]{{1, 3}, {2, 6}, {3, 9}, {4, 12}, {5, 9}, {6, 6}, {7, 3}, {8, 6}, {9, 9}, {10, 12}});
        Job job3 = new Job(3, 100, new int[][]{{1, 1}, {2, 3}, {3, 5}, {4, 7}, {5, 9}, {6, 11}, {7, 1}, {8, 5}, {9, 3}, {10, 9}});
        Job job4 = new Job(4, 100, new int[][]{{1, 2}, {2, 2}, {3, 1}, {4, 1}, {5, 1}, {6, 3}, {7, 6}, {8, 7}, {9, 9}, {10, 12}});
        Job job5 = new Job(5, 100, new int[][]{{1, 5}, {2, 10}, {3, 15}, {4, 12}, {5, 11}, {6, 7}, {7, 3}, {8, 10}, {9, 1}, {10, 9}});
        Job job6 = new Job(6, 100, new int[][]{{1, 1}, {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {7, 1}, {8, 1}, {9, 1}, {10, 1}});
        Job job7 = new Job(7, 100, new int[][]{{1, 7}, {2, 3}, {3, 3}, {4, 3}, {5, 7}, {6, 1}, {7, 10}, {8, 11}, {9, 12}, {10, 1}});
        Job job8 = new Job(8, 100, new int[][]{{1, 2}, {2, 5}, {3, 2}, {4, 5}, {5, 1}, {6, 2}, {7, 6}, {8, 1}, {9, 3}, {10, 8}});
        Job job9 = new Job(9, 100, new int[][]{{1, 1}, {2, 3}, {3, 5}, {4, 4}, {5, 2}, {6, 1}, {7, 3}, {8, 1}, {9, 2}, {10, 9}});
        Job job10 = new Job(10, 100, new int[][]{{1, 1}, {2, 6}, {3, 5}, {4, 3}, {5, 1}, {6, 3}, {7, 1}, {8, 2}, {9, 8}, {10, 7}});


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

        Greedy.getInstance().init(greedyJobs, 10);
        int localMakeSpan = Greedy.getInstance().localSearch(greedyJobs);
        result = "\t[FAILED]";
        assertEquals(137, localMakeSpan);
        result = "\t[PASSED]";
    }


    @Test
    @DisplayName("[JUnit] generateAllPossibleOrder(3 elements)")
    void generateAllPossibleOrder(TestInfo info) {
        message = info.getDisplayName();
        result = "[FAILED]";
        List<Job> list = new LinkedList<>();
        list.add(new Job(1, 1, new int[][]{{1, 1}}));
        list.add(new Job(2, 1, new int[][]{{1, 1}}));
        list.add(new Job(3, 1, new int[][]{{1, 1}}));
        Greedy.getInstance().init(list, 1);

        int[][] resultMatrix = Greedy.getInstance().generateAllPossibleOrder(list);

        //print matrix
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                System.out.print(resultMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("------------------");

        //assert if the matrix contains 3 columns and 6 rows
        assertEquals(3, resultMatrix[0].length);
        assertEquals(6, resultMatrix.length);
        //assert if the matrix contains the rows [1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]
        int[] row1 = {1, 2, 3};
        int[] row2 = {1, 3, 2};
        int[] row3 = {2, 1, 3};
        int[] row4 = {2, 3, 1};
        int[] row5 = {3, 1, 2};
        int[] row6 = {3, 2, 1};
        //assert if any of the above rows is contained in the matrix ( in any order, it can be in whatever rows)
        assertTrue(Arrays.equals(row1, resultMatrix[0]) || Arrays.equals(row1, resultMatrix[1]) || Arrays.equals(row1, resultMatrix[2]) || Arrays.equals(row1, resultMatrix[3]) || Arrays.equals(row1, resultMatrix[4]) || Arrays.equals(row1, resultMatrix[5]));
        assertTrue(Arrays.equals(row2, resultMatrix[0]) || Arrays.equals(row2, resultMatrix[1]) || Arrays.equals(row2, resultMatrix[2]) || Arrays.equals(row2, resultMatrix[3]) || Arrays.equals(row2, resultMatrix[4]) || Arrays.equals(row2, resultMatrix[5]));
        assertTrue(Arrays.equals(row3, resultMatrix[0]) || Arrays.equals(row3, resultMatrix[1]) || Arrays.equals(row3, resultMatrix[2]) || Arrays.equals(row3, resultMatrix[3]) || Arrays.equals(row3, resultMatrix[4]) || Arrays.equals(row3, resultMatrix[5]));
        assertTrue(Arrays.equals(row4, resultMatrix[0]) || Arrays.equals(row4, resultMatrix[1]) || Arrays.equals(row4, resultMatrix[2]) || Arrays.equals(row4, resultMatrix[3]) || Arrays.equals(row4, resultMatrix[4]) || Arrays.equals(row4, resultMatrix[5]));
        assertTrue(Arrays.equals(row5, resultMatrix[0]) || Arrays.equals(row5, resultMatrix[1]) || Arrays.equals(row5, resultMatrix[2]) || Arrays.equals(row5, resultMatrix[3]) || Arrays.equals(row5, resultMatrix[4]) || Arrays.equals(row5, resultMatrix[5]));
        assertTrue(Arrays.equals(row6, resultMatrix[0]) || Arrays.equals(row6, resultMatrix[1]) || Arrays.equals(row6, resultMatrix[2]) || Arrays.equals(row6, resultMatrix[3]) || Arrays.equals(row6, resultMatrix[4]) || Arrays.equals(row6, resultMatrix[5]));

        result = "[PASSED]";
    }

    @Test
    @DisplayName("[JUnit] generateAllPossibleOrder(7 elements)")
    void generateAllPossibleOrder7(TestInfo info) {
        message = info.getDisplayName();
        result = "[FAILED]";
        List<Job> list = new LinkedList<>();
        list.add(new Job(1, 1, new int[][]{{1, 1}}));
        list.add(new Job(2, 1, new int[][]{{1, 1}}));
        list.add(new Job(3, 1, new int[][]{{1, 1}}));
        list.add(new Job(4, 1, new int[][]{{1, 1}}));
        list.add(new Job(5, 1, new int[][]{{1, 1}}));
        list.add(new Job(6, 1, new int[][]{{1, 1}}));
        list.add(new Job(7, 1, new int[][]{{1, 1}}));
        Greedy.getInstance().init(list, 1);

        int[][] resultMatrix = Greedy.getInstance().generateAllPossibleOrder(list);

        //print matrix
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                System.out.print(resultMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("------------------");
        int maxRows = Utils.factorial(list.size());

        //assert if the matrix contains 7 columns and 6 rows
        assertEquals(7, resultMatrix[0].length);
        assertEquals(maxRows, resultMatrix.length);

        //create a boolen variable and set it to false if there some duplicate row in the matrix
        boolean duplicate = false;
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = i + 1; j < resultMatrix.length; j++) {
                if (Arrays.equals(resultMatrix[i], resultMatrix[j])) {
                    duplicate = true;
                }
            }
        }
        //assert if there is no duplicate row in the matrix
        assertFalse(duplicate);

        //the matrix MUST contain only value from 1 to 7 and no other value. Use a boolean variable to check this, and then assert on it.
        boolean containsOnly1to7 = true;
        for (int i = 0; i < resultMatrix.length; i++) {
            for (int j = 0; j < resultMatrix[i].length; j++) {
                if (resultMatrix[i][j] < 1 || resultMatrix[i][j] > 7) {
                    containsOnly1to7 = false;
                }
            }
        }
        //assert if there is no duplicate row in the matrix
        assertTrue(containsOnly1to7);

        //all value from 1 to 7 must be present in the matrix. Use a boolean variable to check this, and then assert on it.
        boolean containsAll1to7 = true;
        for (int i = 1; i <= 7; i++) {
            boolean contains = false;
            for (int j = 0; j < resultMatrix.length; j++) {
                for (int k = 0; k < resultMatrix[j].length; k++) {
                    if (resultMatrix[j][k] == i) {
                        contains = true;
                    }
                }
            }
            if (!contains) {
                containsAll1to7 = false;
            }
        }
        //assert if all value from 1 to 7 are present in the matrix
        assertTrue(containsAll1to7);

        result = "[PASSED]";
    }

    @Test
    @DisplayName("[JUnit] test Greedy (jobs=3, machines=4)")
    void testGreedy3J4M(TestInfo info) {
        message = info.getDisplayName();
        result = "\t[FAILED]";
        Greedy.getInstance().setFullExploration(false);
        List<Job> list = new LinkedList<>();
        Job job1 = new Job(1, 100, new int[][]{{1, 2}, {2, 3}, {3, 3}, {4, 5}});
        Job job2 = new Job(2, 100, new int[][]{{1, 4}, {2, 7}, {3, 4}, {4, 1}});
        Job job3 = new Job(3, 100, new int[][]{{1, 1}, {2, 1}, {3, 3}, {4, 2}});
        ModelLoader.getInstance().addJob(job1);
        ModelLoader.getInstance().addJob(job2);
        ModelLoader.getInstance().addJob(job3);
        List<Job> jobs = ModelLoader.getInstance().getSortedJobs();

        Greedy.getInstance().init(jobs, 4);
        Pair<List<List<Job>>, Integer> solution = Greedy.getInstance().run();
        int size = solution.getLeft().size();
        assertEquals(2, size);
        List<Job> resultList = solution.getLeft().get(0);
        int makespan = solution.getRight();
        assertEquals(19, makespan);
        result = "\t[PASSED]";
    }


    @Test
    @DisplayName("[JUnit] test Greedy (jobs=6, machines=6)")
    void testGreedy6J46(TestInfo info) {
        message = info.getDisplayName();
        result = "\t[FAILED]";
        Greedy.getInstance().setFullExploration(false);
        List<Job> list = new LinkedList<>();
        // consider this creation policy Job job1 = new Job(1, 100, new int[][]{{1, 2}, {2, 3}, {3, 3}, {4, 5}});
        //no create 6 jobs with this stats:
        //job1: 100, 2,5,7,9,1,7
        //job2: 100, 3,4,2,6,3,3
        //job3: 100, 2,3,5,2,1,2
        //job4: 100, 5,1,9,3,8,5
        //job5: 100, 7,3,3,6,7,4
        //job6: 100, 6,6,2,9,6,3
        //add them to the model loader
        Job job1 = new Job(1, 100, new int[][]{{1, 2}, {2, 5}, {3, 7}, {4, 9}, {5, 1}, {6, 7}});
        Job job2 = new Job(2, 100, new int[][]{{1, 3}, {2, 4}, {3, 2}, {4, 6}, {5, 3}, {6, 3}});
        Job job3 = new Job(3, 100, new int[][]{{1, 2}, {2, 3}, {3, 5}, {4, 2}, {5, 1}, {6, 2}});
        Job job4 = new Job(4, 100, new int[][]{{1, 5}, {2, 1}, {3, 9}, {4, 3}, {5, 8}, {6, 5}});
        Job job5 = new Job(5, 100, new int[][]{{1, 7}, {2, 3}, {3, 3}, {4, 6}, {5, 7}, {6, 4}});
        Job job6 = new Job(6, 100, new int[][]{{1, 6}, {2, 6}, {3, 2}, {4, 9}, {5, 6}, {6, 3}});
        ModelLoader.getInstance().addJob(job1);
        ModelLoader.getInstance().addJob(job2);
        ModelLoader.getInstance().addJob(job3);
        ModelLoader.getInstance().addJob(job4);
        ModelLoader.getInstance().addJob(job5);
        ModelLoader.getInstance().addJob(job6);

        List<Job> jobs = ModelLoader.getInstance().getSortedJobs();

        //print all sorted job
        System.out.println(" ----------------------- sorted jobs ----------------------- ");
        for (Job job : jobs) {
            System.out.println(job);
        }
        System.out.println(" ----------------------- xxxxxxxxxxxx ----------------------- ");

        Greedy.getInstance().init(jobs, 6);
        Pair<List<List<Job>>, Integer> solution = Greedy.getInstance().run();
        int size = solution.getLeft().size();
        assertEquals(1, size);
        List<Job> resultList = solution.getLeft().get(0);
        int makespan = solution.getRight();
        assertEquals(6, resultList.size());
        assertEquals(55, makespan);
        result = "\t[PASSED]";
    }


}