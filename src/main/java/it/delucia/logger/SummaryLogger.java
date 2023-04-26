package it.delucia.logger;

import java.io.*;

public class SummaryLogger {
    //implement singleton pattern
    private static SummaryLogger instance = null;
    private File currentLogFile = null;
    private boolean isClosed = true;

    private String sessionName;

    private SummaryLogger() {
    }

    public static SummaryLogger getInstance() {
        if (instance == null) {
            instance = new SummaryLogger();
        }
        return instance;
    }

    public void start(String sessionName) {
        this.sessionName = sessionName;
        createLogsFolder();
        this.currentLogFile = createLogFile(sessionName);
        this.isClosed = false;
    }

    public String getSessionName() {
        return sessionName;
    }

    //create a log file with a name composed by a timestamp and a string argument passed by argument. The file
    //must be created in a folder called 'logs'. It returns the File instance
    public File createLogFile(String name) {
        String fileName = "logs/" + name + "_" + System.currentTimeMillis() + ".log";
        File file = new File(fileName);
        return file;
    }

    //write a string to a file
    public void append(String message) {
        //check if the logger is started

        if (isClosed) {
            throw new IllegalStateException("You must start the logger before writing to it");
        }
        if (this.currentLogFile == null) {
            throw new IllegalStateException("You must start the logger before writing to it");
        }
        //write the message on the currentLogFile, by appending it to the end of the file
        try (FileWriter fw = new FileWriter(this.currentLogFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
    }

    public void close() {
        isClosed = true;
    }

    //return the path of the current log file
    public File getCurrentLogFile() {
        return currentLogFile;
    }

    //create the folder 'logs' if it doesn't exist
    public void createLogsFolder() {
        File logsFolder = new File("logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdir();
        }
    }
}
