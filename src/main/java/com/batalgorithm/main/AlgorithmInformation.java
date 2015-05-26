package com.batalgorithm.main;

/**
 * Параметры алгоритма.
 */
public class AlgorithmInformation {

    private int n;
    private int N_gen;
    private double A;
    private double r;
    private int Qmin;
    private int Qmax;
    private int maxStep;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN_gen() {
        return N_gen;
    }

    public void setN_gen(int n_gen) {
        N_gen = n_gen;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public int getQmin() {
        return Qmin;
    }

    public void setQmin(int qmin) {
        Qmin = qmin;
    }

    public int getQmax() {
        return Qmax;
    }

    public void setQmax(int qmax) {
        Qmax = qmax;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public boolean isValid() {
        return n > 0 && N_gen > 0 && A >= 0 && r >= 0 && Qmin >= 0 && Qmax >= 0 && maxStep > 0;
    }
}
