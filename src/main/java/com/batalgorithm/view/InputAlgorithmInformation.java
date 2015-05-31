package com.batalgorithm.view;

import com.batalgorithm.main.AlgorithmInformation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Диалоговое окно для ввода параметров алгоритма.
 */
public class InputAlgorithmInformation extends JPanel {

    private JTextField inputN;
    private JTextField inputN_gen;
    private JTextField inputA;
    private JTextField inputR;
    private JTextField inputQmin;
    private JTextField inputQmax;
    private JTextField inputMaxStep;

    public InputAlgorithmInformation() {

        JPanel mainPanel = new JPanel();
        JLabel header = new JLabel("Параметры алгоритма: ");
        header.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(header);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel inputFieldsPanel = new JPanel();
        inputFieldsPanel.setBorder(new LineBorder(Color.gray, 1));
        inputFieldsPanel.setLayout(new GridLayout(4, 2));

        initializeInputs();
        setDefaultValuesIntoInputs();

        JPanel inputNMainPanel = new JPanel();
        inputNMainPanel.setLayout(new BoxLayout(inputNMainPanel, BoxLayout.Y_AXIS));
        JLabel nMainLabel = new JLabel("Размер популяции");
        nMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputNPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nLabel = new JLabel("N: ");
        inputNPanel.add(new JLabel("              "));
        inputNPanel.add(nLabel);
        inputNPanel.add(inputN);
        inputNPanel.add(new JLabel("              "));
        inputNMainPanel.add(nMainLabel);
        inputNMainPanel.add(inputNPanel);

        JPanel inputNGenMainPanel = new JPanel();
        inputNGenMainPanel.setLayout(new BoxLayout(inputNGenMainPanel, BoxLayout.Y_AXIS));
        JLabel nGenMainLabel = new JLabel("Количество поколений");
        nGenMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputNGenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nGenLabel = new JLabel("N_gen: ");
        inputNGenPanel.add(new JLabel("              "));
        inputNGenPanel.add(nGenLabel);
        inputNGenPanel.add(inputN_gen);
        inputNGenPanel.add(new JLabel("                    "));
        inputNGenMainPanel.add(nGenMainLabel);
        inputNGenMainPanel.add(inputNGenPanel);

        JPanel inputAMainPanel = new JPanel();
        inputAMainPanel.setLayout(new BoxLayout(inputAMainPanel, BoxLayout.Y_AXIS));
        JLabel aMainLabel = new JLabel("Громкость (в диапазоне от 0 до 1): ");
        aMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputAPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel aLabel = new JLabel("A: ");
        inputAPanel.add(new JLabel("              "));
        inputAPanel.add(aLabel);
        inputAPanel.add(inputA);
        inputAPanel.add(new JLabel("                    "));
        inputAMainPanel.add(aMainLabel);
        inputAMainPanel.add(inputAPanel);

        JPanel inputRMainPanel = new JPanel();
        inputRMainPanel.setLayout(new BoxLayout(inputRMainPanel, BoxLayout.Y_AXIS));
        JLabel rMainLabel = new JLabel("Частота пульсации (в диапазоне от 0 до 1): ");
        rMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputRPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel rLabel = new JLabel("r: ");
        inputRPanel.add(new JLabel("                        "));
        inputRPanel.add(rLabel);
        inputRPanel.add(inputR);
        inputRPanel.add(new JLabel("                    "));
        inputRMainPanel.add(rMainLabel);
        inputRMainPanel.add(inputRPanel);

        JPanel inputQminMainPanel = new JPanel();
        inputQminMainPanel.setLayout(new BoxLayout(inputQminMainPanel, BoxLayout.Y_AXIS));
        JLabel QminMainLabel = new JLabel("Минимальная частота");
        QminMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputQminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel QminLabel = new JLabel("Qmin: ");
        inputQminPanel.add(new JLabel("       "));
        inputQminPanel.add(QminLabel);
        inputQminPanel.add(inputQmin);
        inputQminPanel.add(new JLabel("                    "));
        inputQminMainPanel.add(QminMainLabel);
        inputQminMainPanel.add(inputQminPanel);

        JPanel inputQmaxMainPanel = new JPanel();
        inputQmaxMainPanel.setLayout(new BoxLayout(inputQmaxMainPanel, BoxLayout.Y_AXIS));
        JLabel QmaxMainLabel = new JLabel("Максимальная частота");
        QmaxMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputQmaxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel QmaxLabel = new JLabel("Qmax: ");
        inputQmaxPanel.add(new JLabel("              "));
        inputQmaxPanel.add(QmaxLabel);
        inputQmaxPanel.add(inputQmax);
        inputQmaxPanel.add(new JLabel("                    "));
        inputQmaxMainPanel.add(QmaxMainLabel);
        inputQmaxMainPanel.add(inputQmaxPanel);

        JPanel inputMaxStepMainPanel = new JPanel();
        inputMaxStepMainPanel.setLayout(new BoxLayout(inputMaxStepMainPanel, BoxLayout.Y_AXIS));
        JLabel maxStepMainLabel = new JLabel("Максимальный размер шага при поиске");
        maxStepMainLabel.setAlignmentX(CENTER_ALIGNMENT);
        JPanel inputMaxStepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel maxStepLabel = new JLabel("maxStep: ");
        inputMaxStepPanel.add(new JLabel(""));
        inputMaxStepPanel.add(maxStepLabel);
        inputMaxStepPanel.add(inputMaxStep);
        inputMaxStepMainPanel.add(maxStepMainLabel);
        inputMaxStepMainPanel.add(inputMaxStepPanel);

        inputFieldsPanel.add(inputNMainPanel);
        inputFieldsPanel.add(inputNGenMainPanel);
        inputFieldsPanel.add(inputAMainPanel);
        inputFieldsPanel.add(inputRMainPanel);
        inputFieldsPanel.add(inputQminMainPanel);
        inputFieldsPanel.add(inputQmaxMainPanel);
        inputFieldsPanel.add(inputMaxStepMainPanel);

        mainPanel.add(inputFieldsPanel);

        add(mainPanel);
    }

    public void setDefaultValuesIntoInputs() {
        inputN.setText("20");
        inputN_gen.setText("1000");
        inputA.setText("0.5");
        inputR.setText("0.5");
        inputQmin.setText("1");
        inputQmax.setText("5");
        inputMaxStep.setText("5");
    }

    private void initializeInputs() {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setGroupingUsed(false);
        inputN = new JFormattedTextField(numberInstance);
        inputN_gen = new JFormattedTextField(numberInstance);
        inputA = new JFormattedTextField(numberInstance);
        inputR = new JFormattedTextField(numberInstance);
        inputQmin = new JFormattedTextField(numberInstance);
        inputQmax = new JFormattedTextField(numberInstance);
        inputMaxStep = new JFormattedTextField(numberInstance);

        inputN.setColumns(10);
        inputN_gen.setColumns(10);
        inputA.setColumns(10);
        inputR.setColumns(10);
        inputQmin.setColumns(10);
        inputQmax.setColumns(10);
        inputMaxStep.setColumns(10);
    }

    public AlgorithmInformation getAlgorithmInformation() {
        AlgorithmInformation algorithmInformation = new AlgorithmInformation();
        algorithmInformation.setN(Integer.valueOf(inputN.getText()));
        algorithmInformation.setN_gen(Integer.valueOf(inputN_gen.getText()));
        algorithmInformation.setA(Double.valueOf(inputA.getText()));
        algorithmInformation.setR(Double.valueOf(inputR.getText()));
        algorithmInformation.setQmin(Integer.valueOf(inputQmin.getText()));
        algorithmInformation.setQmax(Integer.valueOf(inputQmax.getText()));
        algorithmInformation.setMaxStep(Integer.valueOf(inputMaxStep.getText()));
        return algorithmInformation;
    }
}
