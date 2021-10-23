package com.imc.workshop.core.limits;

public class Polynom {

    public static PolynomialRoots computePolynomialRoots(double quadraticCoefficient, double linearCoefficient, double constantTerm) {
        double myDelta = linearCoefficient * linearCoefficient - 4 * quadraticCoefficient * constantTerm;

        PolynomialRoots myRoots = new PolynomialRoots();

        if (myDelta > 0) {
            myRoots.theFirstRoot = (-linearCoefficient - Math.sqrt(myDelta)) / (2 * quadraticCoefficient);
            myRoots.theSecondRoot = (-linearCoefficient + Math.sqrt(myDelta)) / (2 * quadraticCoefficient);
        } else {
            // If delta is negative, there is no real root, and the following statement computes the minimum of the polynomial
            myRoots.theFirstRoot = -linearCoefficient / (2 * quadraticCoefficient);
        }

        return myRoots;
    }

    public static class PolynomialRoots {
        double theFirstRoot = Double.NaN;
        double theSecondRoot = Double.NaN;
    }
}