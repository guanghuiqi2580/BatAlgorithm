package com.batalgorithm.view;

import com.batalgorithm.main.AlgorithmInformation;
import com.batalgorithm.main.AlgorithmRunner;
import com.batalgorithm.main.CircuitBoardInformation;

import javax.swing.*;
import java.awt.*;

/**
 * Окно приложения для ввода информации и запуска алгоритма на выполнение.
 */
public class MainWindow extends JFrame {

    private static final String TITLE = "Bat inspired algorithm";

    public MainWindow(AlgorithmRunner runner) throws HeadlessException {
        super(TITLE);
        MainWindow mainWindow = this;

        InputCircuitBoardInformation inputCircuitBoardInformation = new InputCircuitBoardInformation(mainWindow);
        InputAlgorithmInformation inputAlgorithmInformation = new InputAlgorithmInformation();

        JButton runButton = new JButton("Запустить");
        runButton.addActionListener(e -> {
            try {
                CircuitBoardInformation boardInfo = inputCircuitBoardInformation.getCircuitBoardInformation();
                AlgorithmInformation algInfo = inputAlgorithmInformation.getAlgorithmInformation();
                if (boardInfo.isValid() && algInfo.isValid()) {
                    runner.calculate(boardInfo, algInfo);
                } else {
                    new ErrorDialog(mainWindow, "Введены неверные параметры! Проверьте введенную информацию и попробуйте снова.");
                }
            } catch (Exception exc) {
                new ErrorDialog(mainWindow, "Что-то не так.. Проверьте введенную информацию и попробуйте снова.");
            }
        });

        JButton defaultsButton = new JButton("Значения по умолчанию");
        defaultsButton.addActionListener(e -> {
            inputCircuitBoardInformation.setDefaultValuesIntoInputs();
            inputAlgorithmInformation.setDefaultValuesIntoInputs();
        });

        JPanel runButtonPanel = new JPanel();
        runButtonPanel.add(runButton);
        runButtonPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel inputAlgorithmInformationButtonPanel = new JPanel();
        inputAlgorithmInformationButtonPanel.add(inputAlgorithmInformation);

        JPanel inputCircuitBoardInformationButtonPanel = new JPanel();
        inputCircuitBoardInformationButtonPanel.add(inputCircuitBoardInformation);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(runButtonPanel);
        buttonPanel.add(defaultsButton);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(inputCircuitBoardInformationButtonPanel);
        add(inputAlgorithmInformationButtonPanel);
        add(buttonPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setVisible(true);
    }
}
