package it.delucia.plotter;

import it.delucia.logger.SummaryPrinter;

public class TestJSON {

    public static void main(String[] args) {
        //Build a schedule with some event inside and then print it to file "prova.json" using the method available in SummaryPrinter

        Schedule schedule = new Schedule();
        schedule.addEvent(new ScheduledJobArrival("Job1", 1, "some text"));
        schedule.addEvent(new ScheduledJobArrival("Job2", 3, "some other text"));
        schedule.addEvent(new ScheduledResourceLoad("+R", 1, "Diamanti +1, Oro +1"));
        schedule.addEvent(new ScheduledResourceLoad("+R", 3,"some other text"));
        schedule.addEvent(new ScheduledJob("Job1", 1, 0, 3, "some text"));

        new PlotterManager.Builder().prepare(schedule)
                .setFolderName("plots")
                .setFileName("prova")
                .build().printJSON();


    }
}
