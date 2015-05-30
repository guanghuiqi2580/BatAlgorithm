package com.batalgorithm.main;

import com.batalgorithm.view.MainWindow;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) throws IOException {

        Thread currentThread = Thread.currentThread();
        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
        InputStream propertiesStream = contextClassLoader.getResourceAsStream("logging.properties");
        if (propertiesStream != null) {
            LogManager.getLogManager().readConfiguration(propertiesStream);
            propertiesStream.close();
        }
        new MainWindow(new AlgorithmRunner());
    }
}