package it.delucia.algo;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    //ritorna il fattoriale di un numero
    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    //ritorna tutte le possibili permutazioni di un array di n interi
    public static int[][] generateAllPossibleOrder(int[] sequence){
        //complete an integer array with all id of the jobs
        int maxOrders = factorial(sequence.length);
        List<List<Integer>> sequenceList = new ArrayList<>(maxOrders);
        //generate all possible orders
        generateAllPossibleOrder(sequence.length, sequence, sequenceList, 0);
        int[][] allOrders = new int[maxOrders][sequence.length];
        for(int i = 0; i < maxOrders; i++) {
            for(int j = 0; j < sequence.length; j++) {
                allOrders[i][j] = sequenceList.get(i).get(j);
            }
        }
        return allOrders;
    }

    //ritorna tutte le possibili permutazioni di un array di n interi
    public static void generateAllPossibleOrder(int n, int[] elements,List<List<Integer>> sequenceList, int index) {

        if(n == 1) {
            List<Integer> sequence = new ArrayList<>();
            for(int i = 0; i < elements.length; i++) {
                sequence.add(elements[i]);
            }
            sequenceList.add(index, sequence);
        }else {
            for(int i = 0; i < n-1; i++) {
                generateAllPossibleOrder(n-1, elements, sequenceList, index);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                }else {
                    swap(elements, 0, n-1);
                }
            }
            generateAllPossibleOrder(n-1, elements, sequenceList, index+1);
        }

    }

    //scambia due elementi di un array
    public static void swap(int[] sequence, int i, int j) {
        int temp = sequence[i];
        sequence[i] = sequence[j];
        sequence[j] = temp;
    }

    public static void main(String[] args) {
        int[] sequence = {1,2,3};
        int[][] allOrders = generateAllPossibleOrder(sequence);
        for(int i = 0; i < allOrders.length; i++) {
            for(int j = 0; j < allOrders[i].length; j++) {
                System.out.print(" "+allOrders[i][j]);
            }
            System.out.println(" ");
        }
    }
}
