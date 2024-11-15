/*  
Name: Tsugoii
Date: 10/09/2021
Description: utility class that contains two overloaded implementations of a method named checkSorted (determines whether a List object, supplied as a parameter, is in strictly ascending order)
*/

package Project2;

import Project2;
import java.util.*;

// Checks to see if a file is in strictly ascending order
public class OrderedList {
    // Overloaded method using a list
    public static boolean checkSorted(List<Comparable> list) {
        return checkSorted(list, Comparator.naturalOrder());
    }

    // Overloaded method using list and object
    public static boolean checkSorted(List<Comparable> list, Comparator<Comparable> comparator) {
        if (list == null)
            return false;
        if (list.isEmpty() || list.size() == 1)
            return true;
        int diff = 0;
        for (int i = 1; i < list.size(); i++) {
            if (diff > 0)
                return false;
            diff = comparator.compare(list.get(i - 1), list.get(i));
        }
        return diff <= 0;
    }
}