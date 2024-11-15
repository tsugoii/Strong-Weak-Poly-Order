/*  
Name: Tsugoii
Date: 10/09/2021
Description: defines an unchecked exception that contains a constructor that allows a message to be supplied
*/

package Project2;
import Project2;
import java.io.*;

// Adds a user defined exception
public class InvalidPolynomialSyntax extends Exception {
    public InvalidPolynomialSyntax(String error) {
        super("Error Thrown: " + error);
    }
    
}