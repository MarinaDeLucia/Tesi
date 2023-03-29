package it.delucia.exceptions;

public class StepWithNoSolutionException extends Exception{
    public StepWithNoSolutionException() {
        super("Step with no solution");
    }
}
