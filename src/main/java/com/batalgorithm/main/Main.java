package com.batalgorithm.main;

import com.batalgorithm.view.MainWindow;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {

        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        InputStream propertiesStream = contextClassLoader.getResourceAsStream("logging.properties");
        if (propertiesStream != null) {
            LogManager.getLogManager().readConfiguration(propertiesStream);
            propertiesStream.close();
        }
//        System.setProperty("file.encoding", "UTF-8");
        LOG.info("Program has been started.");
        new MainWindow(new AlgorithmRunner());
    }
}