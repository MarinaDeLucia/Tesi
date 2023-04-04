package it.delucia.logger;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryPrinter {
    //singleton pattern
    private static SummaryPrinter instance = null;
    private final String separator = "===============================================================";

    private SummaryPrinter() {
    }

    public static SummaryPrinter getInstance() {
        if (instance == null) {
            instance = new SummaryPrinter();
        }
        return instance;
    }

    //print a start message to a string buffer and then pass it to the SummaryLogger
    public void start(String sessionName) {
        SummaryLogger.getInstance().start(sessionName);
        StringBuilder sb = new StringBuilder();
        //create a date formatted like 04 mar 2020 12:00:00
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        sb.append(separator).append("\n");
        sb.append("Session: ").append(sessionName).append(" at ").append(formattedDate).append("\n");
        sb.append(separator).append("\n");
        SummaryLogger.getInstance().append(sb.toString());

    }

    //end the summary by calling the close/end method of the SummaryLogger
    public void end() {
        //append the ending time in the format like 04 mar 2020 12:00:00
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        SummaryLogger.getInstance().append(separator);
        SummaryLogger.getInstance().append("Session ended at " + formattedDate);
        SummaryLogger.getInstance().append(separator);

        SummaryLogger.getInstance().close();
        //open the file by the default application of the OS
        try {
            Desktop.getDesktop().open(SummaryLogger.getInstance().getCurrentLogFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
