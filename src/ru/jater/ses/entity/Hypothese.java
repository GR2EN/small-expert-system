package ru.jater.ses.entity;

import java.util.HashMap;
import java.util.Map;

public class Hypothese implements Comparable<Hypothese> {

    private String title;
    private Double pAposterior;
    private Map<Integer, Probability> info;

    public Hypothese(String title, double pAposterior) {
        this.title = title;
        this.pAposterior = pAposterior;
        info = new HashMap<>();
    }

    public double getpPositive(int questionId) {
        return info.get(questionId).getpPositive();
    }

    public double getpNegative(int questionId) {
        return info.get(questionId).getpNegative();
    }

    public double getpAposterior() {
        return pAposterior;
    }

    public void setpAposterior(double pAposterior) {
        this.pAposterior = pAposterior;
    }

    public void addQuestion(Integer questionId, Probability prob) {
        info.put(questionId, prob);
    }

    @Override
    public String toString() {
        return "[" + String.format("%.5f", pAposterior) + "] " + title;
    }


    @Override
    public int compareTo(Hypothese hypothese) {
        return this.pAposterior.compareTo(hypothese.getpAposterior());
    }
}
