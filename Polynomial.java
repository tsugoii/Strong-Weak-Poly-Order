/*  
Name: Tsugoii
Date: 10/09/2021
Description: examines a file of polynomials and determines whether the polynomials in that file are in strictly ascending order using two different methods of comparison
*/

package Project2;

import Project2;
import java.util.Arrays;
import java.util.Iterator;

public class Polynomial implements Iterable<Polynomial.Term>, Comparable<Polynomial> {

    public TermListItem first;
    public TermListItem last;

    // constructor
    public Polynomial(String format) throws InvalidPolynomialSyntax {
        if (format == null || format.trim().isEmpty())
            throw new InvalidPolynomialSyntax("No polynomial given.");

        // 4.0 3 2.5 1 8.0 0
        String[] split = format.trim().split("\\s+");
        // [4.0, 3, 2.5, 1, 8.0, 0]

        // Some lovely error checking
        if (split.length % 2 != 0)
            throw new InvalidPolynomialSyntax("Mismatched number of coefficients and exponents.");

        double[][] arr = new double[split.length / 2][2];

        try {
            for (int i = 0; i < split.length; i++)
                arr[i / 2][i % 2] = Double.parseDouble(split[i]); // i % 2 says whether index is even or odd - odd is
                                                                  // exponent
        } catch (NumberFormatException ex) {
            throw new InvalidPolynomialSyntax("Value could not be parsed into double.");
        }

        // [[4.0, 3], [2.5, 1], [8.0, 0]]
        if (arr.length > 1)
            for (int i = 1; i < arr.length; i++) {
                if (arr[i - 1][1] < arr[i][1])
                    throw new InvalidPolynomialSyntax("Exponents must be in descending order.");
                else if (arr[i - 1][1] == arr[i][1])
                    throw new InvalidPolynomialSyntax("Please combine like terms.");
            }

        for (double[] row : arr)
            addTerm(row[0], row[1]);
    }

    // compares polynomials
    @Override
    public int compareTo(Polynomial other) {
        int diff = 0;
        Iterator<Term> selfIterator = iterator();
        Iterator<Term> otherIterator = other.iterator();

        while (diff == 0) {
            if (!selfIterator.hasNext() && !otherIterator.hasNext())
                return 0;
            if (!selfIterator.hasNext())
                return -1;
            if (!otherIterator.hasNext())
                return 1;
            Term x = selfIterator.next();
            Term y = otherIterator.next();
            diff = x.compareTo(y);
        }
        return diff;
    }

    // iterates across the polynomial
    public Iterator<Term> iterator() {
        return new PolynomialIterator(first);
    }

    // puts polynomials to string and formats
    public String toString() {
        String result = "";
        Iterator<Term> iterator = iterator();
        while (iterator.hasNext()) {
            result += iterator.next().toString();
            if (iterator.hasNext())
                result += " + ";
        }
        return result == "" ? null : result;
    }

    // a lovely single linked list
    // | Data | Pointer | -> | Data | Pointer | ~~~~~> | Data | null |
    private void addTerm(double coefficient, double exponent) {
        TermListItem newNode = new TermListItem(coefficient, exponent);

        if (first == null) { // newNode is first in list
            first = newNode;
            last = newNode;
        } else { // there are already nodes in the list
            last.SetNext(newNode); // add one to end of list
            last = newNode; // update last to reference the new last node
        }
    }

    public static class Term implements Comparable<Term> {
        private double coefficient;
        private double exponent;

        public Term(double co, double exp) {
            coefficient = co;
            exponent = exp;
        }

        public double GetCoefficient() {
            return coefficient;
        }

        public double GetExponent() {
            return exponent;
        }

        public String toString() {
            String result = "";
            if (coefficient != 0) {
                result += coefficient;
                if (exponent != 0) {
                    result += "x";
                    if (exponent != 1)
                        result += "^" + exponent;
                }
            }
            return result == "" ? null : result;
        }

        @Override
        public int compareTo(Term other) {
            double diff = GetExponent() - other.GetExponent();
            if (diff == 0)
                diff = GetCoefficient() - other.GetCoefficient();

            if (diff > 0)
                return 1;

            if (diff < 0)
                return -1;

            return 0;
        }
    }

    public static class TermListItem {
        private TermListItem next;
        public Term Data;

        public TermListItem(double co, double exp) {
            Data = new Term(co, exp);
        }

        public TermListItem GetNext() {
            return next;
        }

        public void SetNext(TermListItem nextNode) {
            next = nextNode;
        }
    }

    public class PolynomialIterator implements Iterator<Term> {
        private TermListItem current;

        public PolynomialIterator(TermListItem first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Term next() {
            Term temp = current.Data;
            current = current.GetNext();
            return temp;
        }
    }
}