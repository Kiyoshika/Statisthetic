
import java.awt.Color;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zach
 */
public class Results extends javax.swing.JFrame {

   Vector vecValues = new Vector();
   
    public Results(Vector varVec, String analysisType) {
        initComponents();
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableModel dtm = new DefaultTableModel();
        resultsTable.setModel(dtm);
        
        //different analyses
        if (analysisType.equals("Describe")) {
         setTitle("Describe Data");
        //Add column for variables
        dtm.addColumn("Variables");
        int vectorSize = varVec.size();
        dtm.setRowCount(vectorSize);
        
        for (int i = 0; i < vectorSize; i++) {
            dtm.setValueAt(varVec.get(i), i, 0);
        }
        
            //Max, Min, Mean, Median, StDev, Variance
            dtm.addColumn("Mean"); //index 1
            dtm.addColumn("Std Dev"); //index 2
            dtm.addColumn("Variance"); //index 3
            dtm.addColumn("Minimum"); //index 4
            dtm.addColumn("1st Quart"); //index 5
            dtm.addColumn("Median"); //index 6
            dtm.addColumn("3rd Quart"); //index 7
            dtm.addColumn("Maximum"); //index 8
            //get mean for each variable
            MathFunctions mf = new MathFunctions();
            for (int i = 0; i < vectorSize; i++) {
                dtm.setValueAt(mf.Mean((String)varVec.get(i)), i, 1);
                dtm.setValueAt(mf.StDev((String)varVec.get(i)), i, 2);
                float sd = mf.StDev((String)varVec.get(i));
                dtm.setValueAt(Float.toString(sd*sd), i, 3);
                dtm.setValueAt(mf.Minimum((String)varVec.get(i)), i, 4);
                dtm.setValueAt(mf.FirstQuart((String)varVec.get(i)), i, 5);
                dtm.setValueAt(mf.Median((String)varVec.get(i)), i, 6);
                dtm.setValueAt(mf.ThirdQuart((String)varVec.get(i)), i, 7);
                dtm.setValueAt(mf.Maximum((String)varVec.get(i)), i, 8);
            }

        }
        else if (analysisType.equals("Correlation")) {
            setTitle("Correlation");
              //Add column for variables
                dtm.addColumn("Variables");
                int vectorSize = varVec.size();
                dtm.setRowCount(vectorSize);

                for (int i = 0; i < vectorSize; i++) {
                    dtm.setValueAt(varVec.get(i), i, 0);
                }
                MathFunctions mf = new MathFunctions();
                dtm.addColumn("Correlation");
                dtm.setValueAt(mf.Correlation((String)varVec.get(0), (String)varVec.get(1)), 1, 1);
        }
        else if (analysisType.equals("VarianceTest")) {
            setTitle("Levene's Variance Test");
            //Add column for variables
                dtm.addColumn("Variables");
                int vectorSize = varVec.size();
                dtm.setRowCount(vectorSize);

                String[] names = new String[vectorSize];
                for (int i = 0; i < vectorSize; i++) {
                    dtm.setValueAt(varVec.get(i), i, 0);
                    names[i] = (String)varVec.get(i);
                }
                
                MathFunctions mf = new MathFunctions();
                //Returns an array containing: test_statistic, df1 and df2
                float[] testData = mf.VarianceTest(names);
                dtm.addColumn("P-Value (Approximate)");
                //Use test_statistic, df1 and df2 to get p-value
                float p_value = mf.FDist(testData[0], testData[1], testData[2]);
                dtm.setValueAt(p_value, 1, vectorSize-1);
        }
        else if (analysisType.equals("ANOVATest")) {
            setTitle("ANOVA Test");
            //Add column for variables
                dtm.addColumn("Variables");
                int vectorSize = varVec.size();
                dtm.setRowCount(vectorSize);

                String[] names = new String[vectorSize];
                for (int i = 0; i < vectorSize; i++) {
                    dtm.setValueAt(varVec.get(i), i, 0);
                    names[i] = (String)varVec.get(i);
                }
                
                MathFunctions mf = new MathFunctions();
                //Returns an array containing: test_statistic, df1 and df2
                float[] testData = mf.ANOVA(names);
                dtm.addColumn("P-Value (Approximate)");
                //Use test_statistic, df1 and df2 to get p-value
                float p_value = mf.FDist(testData[0], testData[1], testData[2]);
                dtm.setValueAt(p_value, 1, vectorSize-1);
        }
        else if (analysisType.equals("NormalityTest")) {
            setTitle("Normality Test (Lilliefor's KS)");
            //Add column for variables
                dtm.addColumn("Variables");
                int vectorSize = varVec.size();
                dtm.setRowCount(vectorSize);
                
                float critOne = 0f;
                float critTwo = 0f;

                String[] names = new String[vectorSize];
                for (int i = 0; i < vectorSize; i++) {
                    dtm.setValueAt(varVec.get(i), i, 0);
                    names[i] = (String)varVec.get(i);
                }
                
                MathFunctions mf = new MathFunctions();
                //Returns an array containing: test_statistic, df1 and df2
                float[] testStatistics = mf.NormalityTest(names);
                dtm.addColumn("Test Statistic");
                dtm.addColumn("Significant at 0.05");
                dtm.addColumn("Significant at 0.01");
                
                int sampleSize = mf.SampleSize(names[0]);
                //Display test statistics
                for (int i = 0; i < testStatistics.length; i++) {
                    dtm.setValueAt(testStatistics[i], i, 1);
                    
                    //get critical value from Lilliefor's table
                    //0.05
                    switch(sampleSize) {
                        case 4:
                            critOne = 0.381f;
                            break;
                            
                        case 5:
                            critOne = 0.337f;
                            break;
                            
                        case 6:
                            critOne = 0.319f;
                            break;
                            
                        case 7:
                            critOne = 0.300f;
                            break;
                            
                        case 8:
                            critOne = 0.285f;
                            break;
                            
                        case 9:
                            critOne = 0.271f;
                            break;
                            
                        case 10:
                            critOne = 0.258f;
                            break;
                            
                        case 11:
                            critOne = 0.249f;
                            break;
                            
                        case 12:
                            critOne = 0.242f;
                            break;
                            
                        case 13:
                            critOne = 0.234f;
                            break;
                            
                        case 14:
                            critOne = 0.227f;
                            break;
                            
                        case 15:
                            critOne = 0.220f;
                            break;
                            
                        case 16:
                            critOne = 0.213f;
                            break;
                        
                        case 17:
                            critOne = 0.206f;
                            break;
                            
                        case 18:
                            critOne = 0.200f;
                            break;
                            
                        case 19:
                            critOne = 0.195f;
                            break;
                            
                        case 20:
                            critOne = 0.190f;
                            break;
                            
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                            critOne = 0.180f;
                            break;
                            
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                            critOne = 0.161f;
                            break;
                        default:
                            critOne = 0.886f / (float)Math.sqrt(sampleSize);
                            break;
                    }
                    
                    //0.01
                    switch(sampleSize) {
                        case 4:
                            critOne = 0.417f;
                            break;
                            
                        case 5:
                            critOne = 0.405f;
                            break;
                            
                        case 6:
                            critOne = 0.364f;
                            break;
                            
                        case 7:
                            critOne = 0.348f;
                            break;
                            
                        case 8:
                            critOne = 0.331f;
                            break;
                            
                        case 9:
                            critOne = 0.311f;
                            break;
                            
                        case 10:
                            critOne = 0.294f;
                            break;
                            
                        case 11:
                            critOne = 0.284f;
                            break;
                            
                        case 12:
                            critOne = 0.275f;
                            break;
                            
                        case 13:
                            critOne = 0.268f;
                            break;
                            
                        case 14:
                            critOne = 0.261f;
                            break;
                            
                        case 15:
                            critOne = 0.257f;
                            break;
                            
                        case 16:
                            critOne = 0.250f;
                            break;
                        
                        case 17:
                            critOne = 0.245f;
                            break;
                            
                        case 18:
                            critOne = 0.239f;
                            break;
                            
                        case 19:
                            critOne = 0.235f;
                            break;
                            
                        case 20:
                            critOne = 0.231f;
                            break;
                            
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                            critOne = 0.203f;
                            break;
                            
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                            critOne = 0.187f;
                            break;
                        default:
                            critOne = 1.031f / (float)Math.sqrt(sampleSize);
                            break;
                    }
                    
                    //Test 0.05 sig
                    if (testStatistics[i] > critOne) {
                        dtm.setValueAt("Yes", i, 2);
                    } else {
                        dtm.setValueAt("No", i, 2);
                    }
                    //Test 0.01 sig
                    if (testStatistics[i] > critTwo) {
                        dtm.setValueAt("Yes", i, 3);
                    } else {
                        dtm.setValueAt("No", i, 3);
                    }
                }
                
        }
    }

    private Results() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("resultsFrame"); // NOI18N

        resultsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(resultsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Results.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Results.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Results.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Results.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Results().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable resultsTable;
    // End of variables declaration//GEN-END:variables
}
