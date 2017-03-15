package com.cj.worldbank;


public class CoefficientCorrelation {
    private Double[][] scores;
    private int pairs;
    private Double sumOfProductsOfPairs;
    private Double sumOfXScores;
    private Double sumOfYScores;
    private Double sumOfXScoresSquared;
    private Double sumOfYScoresSquared;

    public CoefficientCorrelation(Double[][] scores) {
        this.scores = scores;
        this.pairs = scores.length;
        this.sumOfProductsOfPairs = calcSumOfProductsOfPairs(scores);
        this.sumOfXScores = calcSumOfXScores(scores);
        this.sumOfYScores = calcSumOfYScores(scores);
        this.sumOfXScoresSquared = calcSumOfXScoresSquared(scores);
        this.sumOfYScoresSquared = calcSumOfYScoresSquared(scores);
    }

    public Double calculate() {
        Double topOfEquation = pairs * sumOfProductsOfPairs - sumOfXScores * sumOfYScores;
        Double bottomOfEquation = Math.sqrt((pairs * sumOfXScoresSquared - Math.pow(sumOfXScores, 2)) *
                                            (pairs * sumOfYScoresSquared - Math.pow(sumOfYScores, 2)));
        return topOfEquation / bottomOfEquation;
    }

    private Double calcSumOfProductsOfPairs(Double[][] scores) {
        Double result = 0.0;
        for (int i=0; i < scores.length; i++) {
            Double product = scores[i][0] * scores[i][1]; // {{23, 52}, {89, 87}, {87, 91}}
            result += product;
        }
        return result;
    }

    private Double calcSumOfXScores(Double[][] scores) {
        Double result = 0.0;
        for (int i=0; i < scores.length; i++) { // {{23, 52}, {89, 87}, {87, 91}}
            result += scores[i][0];
        }
        return result;
    }

    private Double calcSumOfYScores(Double[][] scores) {
        Double result = 0.0;
        for (int i=0; i < scores.length; i++) { // {{23, 52}, {89, 87}, {87, 91}}
            result += scores[i][1];
        }
        return result;
    }

    private Double calcSumOfXScoresSquared(Double[][] scores) {
        Double result = 0.0;
        for (int i=0; i < scores.length; i++) { // {{23, 52}, {89, 87}, {87, 91}}
            result += Math.pow(scores[i][0], 2);
        }
        return result;
    }

    private Double calcSumOfYScoresSquared(Double[][] scores) {
        Double result = 0.0;
        for (int i=0; i < scores.length; i++) { // {{23, 52}, {89, 87}, {87, 91}}
            result += Math.pow(scores[i][1], 2);
        }
        return result;
    }
}
