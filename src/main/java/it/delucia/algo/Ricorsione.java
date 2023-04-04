package it.delucia.algo;

public class Ricorsione {


    public static void main(String[] args) {
        int[] array = new int[20];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1000) + 1;
        }
        System.out.println("Array: ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
        System.out.println("Minimo: " + minimo(array, 0, array.length - 1));
    }

    public static int minimo(int[] array, int start, int end) {
        if (start == end) {
            return array[start];
        }
        int middle = (start + end) / 2;
        int min1 = minimo(array, start, middle);
        int min2 = minimo(array, middle + 1, end);
        return Math.min(min1, min2);
    }

    //versione semplificata ricorsiva di "minimo" ma che non effettua la divisione in due parti
    public static int minimoSemplificato(int[] array, int start, int end) {
        if (start == end) {
            return array[start];
        }
        int min = minimoSemplificato(array, start + 1, end);
        return Math.min(array[start], min);
    }

}
