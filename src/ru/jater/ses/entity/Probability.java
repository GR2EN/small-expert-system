package ru.jater.ses.entity;

public class Probability {

    private double pPositive;
    private double pNegative;

    public Probability(double pPositive, double pNegative) {
        this.pPositive = pPositive;
        this.pNegative = pNegative;
    }

    public double getpPositive() {
        return pPositive;
    }

    public double getpNegative() {
        return pNegative;
    }

}
