/*  
Name: Tsugoii
Date: 10/09/2021
Description: Main
*/

package Project2;

import Project2;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// GUI time
public class Project2 extends JFrame implements ActionListener {
    JMenuBar mb;
    JMenu file;
    JMenuItem open;
    JTextArea ta;

    Project2() {
        open = new JMenuItem("Open File");
        open.addActionListener(this);
        file = new JMenu("File");
        file.add(open);
        mb = new JMenuBar();
        mb.setBounds(0, 0, 800, 20);
        mb.add(file);
        ta = new JTextArea(800, 800);
        ta.setBounds(0, 20, 800, 800);
        add(mb);
        add(ta);
    }

    // Driver for everything else
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == open) {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filepath));
                    List<Comparable> inputPolynomialsLine = new ArrayList<Comparable>();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        inputPolynomialsLine.add(new Polynomial(line));
                    }
                    for (int t = 0; t < inputPolynomialsLine.size(); t++) {
                        ta.append(inputPolynomialsLine.get(t).toString());
                        ta.append("\n");
                    }

                    ta.append("Polynomials are in strong order: ");
                    ta.append(String.valueOf(OrderedList.checkSorted(inputPolynomialsLine)));

                    ta.append("\nPolynomials are in weak order: ");

                    Comparator<Comparable> namedLambda = (x, y) -> {
                        double diff = ((Polynomial.Term) x).GetExponent() - ((Polynomial.Term) y).GetExponent();
                        return diff > 0 ? 1 : diff < 0 ? -1 : 0;
                    }; 
                    
                    // Oh boy. Comparing these orders takes wayyy too long
                    boolean weakOrder = true;
                    if (inputPolynomialsLine.size() > 1)
                        for (int v = 1; v < inputPolynomialsLine.size(); v++) {
                            Iterator<Polynomial.Term> x = ((Polynomial) inputPolynomialsLine.get(v - 1)).iterator();
                            Iterator<Polynomial.Term> y = ((Polynomial) inputPolynomialsLine.get(v)).iterator();
                            while (weakOrder)
                                if (!x.hasNext()) {
                                    break;
                                } else if (!y.hasNext()) {
                                    weakOrder = false;
                                } else {
                                    Polynomial.Term xTerm = x.next();
                                    Polynomial.Term yTerm = y.next();

                                    double diff = namedLambda.compare(xTerm, yTerm);
                                    if (diff < 0)
                                        break;
                                    if (diff > 0) {
                                        weakOrder = false;
                                        break;
                                    }
                                }
                            if (!weakOrder)
                                break;
                        }

                    ta.append(String.valueOf(weakOrder));
                    ta.append("\n");

                    br.close();
                } catch (InvalidPolynomialSyntax exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }

            }
        }
    }

    public static void main(String[] args) {
        Project2 om = new Project2();
        om.setSize(500, 500);
        om.setLayout(null);
        om.setVisible(true);
        om.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}