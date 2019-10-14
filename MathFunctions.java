
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JOptionPane;

public class MathFunctions {
    
    private float[] ConvertInput(String getVars) {
         String[] main = ArrayMaps.columnData.get(getVars);
        //start at one to exclude column name
        String[] input = new String[main.length-1];
        System.out.println(main.length);
        System.out.println(input.length);
        for (int i = 1; i < main.length; i++) {
            input[i-1] = main[i];
        }

        float[] values = new float[input.length];
        
        for (int i = 0; i < input.length; i++) {
            try {
                values[i] = Float.parseFloat(input[i]);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "One of your variables could not be converted to float.");
        }

    }
        
        return values;
}
    
    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
}
    
    public float Beta(float lower, float upper, float x, float y) {
        int observations = 100000;
        float delta = (upper - lower) / (float)observations;
 
        float sum = 0;
        float current_value = lower;
        float prior_value = 0f;
        
        //get initial value for trapezoid approximation method
            //sum += (delta/2)*Math.pow(current_value, x-1.0)*Math.pow(1-current_value,y-1.0);
            prior_value = (float) (Math.pow(current_value, x-1.0)*Math.pow(1-current_value,y-1.0));
            current_value += delta;
            
       //iterate the rest
        while (current_value < upper) {
            sum += (delta)*( (Math.pow(current_value, x-1.0)*Math.pow(1-current_value,y-1.0) + prior_value) / 2);
            prior_value = (float) (Math.pow(current_value, x-1.0)*Math.pow(1-current_value,y-1.0));
            current_value += delta;
        }
        
        return sum;
    }
    
    public float ErrorFunction(float lower, float upper, float x) {
        int observations = 100000;
        float delta = (upper - lower) / (float)observations;
 
        float sum = 0;
        float current_value = lower;
        float prior_value = 0f;
        
        //get initial value for trapezoid approximation method
            //sum += (delta/2)*Math.pow(current_value, x-1.0)*Math.pow(1-current_value,y-1.0);
            prior_value = (float) (Math.pow(Math.E,-1*Math.pow(current_value,2)));
            current_value += delta;
            
       //iterate the rest
        while (current_value < upper) {
            sum += (delta)*( ((Math.pow(Math.E,-1*Math.pow(current_value,2))) + prior_value) / 2);
            prior_value = (float) (Math.pow(Math.E, -1*Math.pow(current_value, 2)));
            current_value += delta;
        }
        
        return (float) (sum*(2/Math.sqrt(Math.PI)));
    }
    
    public float CumulativeNormal(float x, float mean, float sd) {
        float erfValue;
        if (x - mean < 0) {
            erfValue = ErrorFunction((x - mean) / (sd*(float)Math.sqrt(2)), 0f, x);
        } else {
            erfValue = ErrorFunction(0f, (x - mean) / (sd*(float)Math.sqrt(2)), x);
        }
        
        //P(X <= x)
        return (float) ((1f + erfValue) / 2f);
    }
    
    public float FDist(float test_stat, float df1, float df2) {
        //There are issues if you don't round the df/2 when passing to beta function 
        float df1_half = round(df1/2.0f,5);
        float df2_half = round(df2/2.0f,5);
        //Get infinity error if we start with 0, so use a small value to "mimic" zero
        float beta = Beta(0.000001f, 1.0f, df1_half, df2_half);
        
        float current_value = test_stat;
        float delta = 0.0035f; //since we're going to positive infinity, we can use an arbitrary small step 
        float upper = 20.0f;
        float sum = 0;
        float prior_value = 0;
        
        //get initial value for trapezoid approximation method
            //sum += (delta/2)*Math.sqrt((Math.pow(df1*current_value,df1)*Math.pow(df2,df2)) / (Math.pow(df1*current_value + df2, df1 + df2))) / (current_value*beta);
            prior_value = (float) (Math.sqrt((Math.pow(df1*current_value,df1)*Math.pow(df2,df2)) / (Math.pow(df1*current_value + df2, df1 + df2))) / (current_value*beta));
            current_value += delta;
        
        //iterate the rest
        while (current_value < upper) {
            sum += (delta)*( ((Math.sqrt((Math.pow(df1*current_value,df1)*Math.pow(df2,df2)) / (Math.pow(df1*current_value + df2, df1 + df2))) / (current_value*beta)) + prior_value) / 2 );
            prior_value = (float) (Math.sqrt((Math.pow(df1*current_value,df1)*Math.pow(df2,df2)) / (Math.pow(df1*current_value + df2, df1 + df2))) / (current_value*beta));
            current_value += delta;
            
        }
        
        return sum;
    }
    
    public float Mean(String getVars) {
       
        float values[] = ConvertInput(getVars);
        
        float sum = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
        return sum/values.length;
}
    
    public int SampleSize(String getVars) {
        float values[] = ConvertInput(getVars);
        
        return values.length;
    }
    
    private float GetMean(float[] values) {
        
        float sum = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
        return sum/values.length;
}
    
    public float StDev(String getVars) {
        
        float[] values = ConvertInput(getVars);
        
        float sum = 0;
        float mean = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
        mean = sum/values.length;
        System.out.println("Mean: " + mean);
        float numerator = 0;
        for (int i = 0; i < values.length; i++) {
            numerator += (values[i] - mean)*(values[i] - mean);
        }
        
        System.out.println("Numerator: " + numerator);
        
        return (float)Math.sqrt((double)numerator/(values.length-1));
}
     private float GetStDev(float[] values) {

        float sum = 0;
        float mean = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }
        mean = sum/values.length;
        System.out.println("Mean: " + mean);
        float numerator = 0;
        for (int i = 0; i < values.length; i++) {
            numerator += (values[i] - mean)*(values[i] - mean);
        }
        
        System.out.println("Numerator: " + numerator);
        
        return (float)Math.sqrt((double)numerator/(values.length-1));
}
     
     public float Minimum(String getVars) {
        
        float[] values = ConvertInput(getVars);
          
        float min = values[0];
 
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }
        
        return min;
}
     
      public float Maximum(String getVars) {
        
        float[] values = ConvertInput(getVars);
        
        float max = values[0];
 
        for (int i = 1; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        
        return max;
}
      
        public float Median(String getVars) {
        
            
         float[] values = ConvertInput(getVars);
        //Sort in ascending order
        Arrays.sort(values);
        float med = -1;
        if (values.length % 2 == 0) {
            //if length is EVEN
            int pos = (int)(values.length / 2);
            med = (values[pos] + values[pos-1]) / 2;
        } else {
            //if length is ODD
            int pos = (int)(values.length / 2);
            med = values[pos];
        }
        
        return med;
}
      
        public float FirstQuart(String getVars) {
        
        float[] values = ConvertInput(getVars);
        
        //Sort in ascending order
        Arrays.sort(values);
        float med = -1;
        int pos = -1;
        if (values.length % 2 == 0) {
            //if length is EVEN
            pos = (int)(values.length / 2);
            med = (values[pos] + values[pos-1]) / 2;
        } else {
            //if length is ODD
            pos = (int)Math.ceil(values.length / 2);
            med = values[pos];
        }
        
        //get lower half
        float[] lower = new float[pos];
        for (int i = 0; i < pos; i++) {
            lower[i] = values[i];
        }
        
        Arrays.sort(lower);
        med = -1;
        pos = -1;
        if (lower.length % 2 == 0) {
            //if length is EVEN
            pos = (int)(lower.length / 2);
            med = (lower[pos] + lower[pos-1]) / 2;
        } else {
            //if length is ODD
            pos = (int)(lower.length / 2);
            med = lower[pos-1];
        }
        return med;
}
          public float ThirdQuart(String getVars) {
       
        float[] values = ConvertInput(getVars);
               
        //Sort in ascending order
        Arrays.sort(values);
        float med = -1;
        boolean isEven = false;
        int pos = -1;
        if (values.length % 2 == 0) {
            //if length is EVEN
            pos = (int)(values.length / 2);
            med = (values[pos] + values[pos-1]) / 2;
            isEven = true;
        } else {
            //if length is ODD
            pos = (int)(values.length / 2);
            med = values[pos-1];
        }
        
        //get upper half
        System.out.println("Position: " + pos);
        float[] upper = new float[pos];
        int ind = 0;
        if (isEven) {
            for (int i = pos; i < values.length; i++) {
                upper[ind] = values[i];
                ind++;
            }
        } else {
            for (int i = pos+1; i < values.length; i++) {
                upper[ind] = values[i];
                ind++;
            }
        }
        
        Arrays.sort(upper);
        med = -1;
        pos = -1;
        if (upper.length % 2 == 0) {
            //if length is EVEN
            pos = (int)(upper.length / 2);
            med = (upper[pos] + upper[pos-1]) / 2;
        } else {
            //if length is ODD
            pos = (int)(upper.length / 2);
            med = upper[pos-1];
        }
        return med;
}
      
        public float Correlation(String getX1, String getX2) {
        String[] x1 = ArrayMaps.columnData.get(getX1);
        String[] x2 = ArrayMaps.columnData.get(getX2);
        //start at one to exclude column name
        String[] x1_input = new String[x1.length-1];
        String[] x2_input = new String[x2.length-1];
 
        for (int i = 1; i < x1.length; i++) {
            x1_input[i-1] = x1[i];
        }
        
        for (int i = 1; i < x2.length; i++) {
            x2_input[i-1] = x2[i];
        }

        float[] x1_vals = new float[x1_input.length];
        float[] x2_vals = new float[x2_input.length];
        
        for (int i = 0; i < x1_input.length; i++) {
            try {
                x1_vals[i] = Float.parseFloat(x1_input[i]);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "One of your variables could not be converted to float.");
        }

    }
        
        for (int i = 0; i < x2_input.length; i++) {
            try {
                x2_vals[i] = Float.parseFloat(x2_input[i]);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "One of your variables could not be converted to float.");
        }

    }
     
        //get means
        float x1_sum = 0;
        for (int i = 0; i < x1_vals.length; i++) {
            x1_sum += x1_vals[i];
        }
        float x1_mean = x1_sum/x1_vals.length;
        
        float x2_sum = 0;
        for (int i = 0; i < x2_vals.length; i++) {
            x2_sum += x2_vals[i];
        }
        float x2_mean = x2_sum/x2_vals.length;
        
        //compute (partial) covariance
        float p_covar = 0;
            //we assume x1 and x2 are same size
        for (int i = 0; i < x1_vals.length; i++) {
            p_covar += (x1_vals[i] - x1_mean)*(x2_vals[i] - x2_mean);
        }
        
        //compute (partial) std devs
        x1_sum = 0;
        for (int i = 0; i < x1_vals.length; i++) {
            x1_sum += (x1_vals[i] - x1_mean)*(x1_vals[i] - x1_mean);
        }
        
        x2_sum = 0;
        for (int i = 0; i < x2_vals.length; i++) {
            x2_sum += (x2_vals[i] - x2_mean)*(x2_vals[i] - x2_mean);
        }

        
        //compute correlation
        return (float) (p_covar / (Math.sqrt(x2_sum)*Math.sqrt(x1_sum)));
}
        
         public float[] VarianceTest(String[] getVars) {
        String[][] data = new String[getVars.length][1];
        
        for (int i = 0; i < data.length; i++) {
            data[i] = ArrayMaps.columnData.get(getVars[i]);
        }
        
        float n_data[][] = new float[data.length][data[0].length-1];
        
        //convert data to float
        try {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length-1; j++) {
                    n_data[i][j] = Float.parseFloat(data[i][j+1]);
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Some of your data may not be converted to numeric values.");
        }
        
        float[] group_means = new float[n_data.length];
      
        for (int i = 0; i < group_means.length; i++) {
            group_means[i] = GetMean(n_data[i]);
        }
        
        //convert data into absolute value difference from their groups and perform ANOVA
        float[][] a_data = new float[n_data.length][n_data[0].length];
        for (int i = 0; i < n_data.length; i++) {
            for (int j = 0; j < n_data[0].length; j++) {
                a_data[i][j] = Math.abs(n_data[i][j] - group_means[i]);
            }
        }
        
        //get absolute group means
        float[] abs_group_means = new float[group_means.length];
        for (int i = 0; i < abs_group_means.length; i++) {
            abs_group_means[i] = GetMean(a_data[i]);
        }
        
        //get absolute group total mean
        float total_group[] = new float[a_data.length*a_data[0].length];
        int t_ind = 0;
        for (int i = 0; i < a_data.length; i++) {
            for (int j = 0; j < a_data[0].length; j++) {
                total_group[t_ind] = a_data[i][j];
                t_ind++;
            }
        }
        
        float total_mean = GetMean(total_group);
        
        //k - 1 degrees of freedom
        float df1 = (a_data.length - 1);
        
        //N - k degrees of freedom
        float df2 = (a_data.length*a_data[0].length) - a_data.length;
        
        //Total sum of squares
        float SST = 0;
        for (int i = 0; i < total_group.length; i++) {
            SST += Math.pow(total_group[i]-total_mean,2);
        }
        
        //Sum of squares within groups
        float SSW = 0;
        for (int g = 0; g < a_data.length; g++) {
            for (int i = 0; i < a_data[0].length; i++) {
                SSW += Math.pow(a_data[g][i] - abs_group_means[g],2);
            }
        }
        
        //Sum of squares between groups
        float SSB = SST - SSW;
        
        //return F statistic, df1 and df2
        float varData[] = new float[3];
        varData[0] = (SSB / df1) / (SSW / df2);
        varData[1] = df1;
        varData[2] = df2;
        return varData;
}
         
         public float[] ANOVA(String[] getVars) {
        String[][] data = new String[getVars.length][1];
        
        for (int i = 0; i < data.length; i++) {
            data[i] = ArrayMaps.columnData.get(getVars[i]);
        }
        
        float n_data[][] = new float[data.length][data[0].length-1];
        
        //convert data to float
        try {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length-1; j++) {
                    n_data[i][j] = Float.parseFloat(data[i][j+1]);
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Some of your data may not be converted to numeric values.");
        }
        
        float[] group_means = new float[n_data.length];
      
        for (int i = 0; i < group_means.length; i++) {
            group_means[i] = GetMean(n_data[i]);
        }
        
        //get absolute group means
        float[] abs_group_means = new float[group_means.length];
        for (int i = 0; i < abs_group_means.length; i++) {
            abs_group_means[i] = GetMean(n_data[i]);
        }
        
        //get absolute group total mean
        float total_group[] = new float[n_data.length*n_data[0].length];
        int t_ind = 0;
        for (int i = 0; i < n_data.length; i++) {
            for (int j = 0; j < n_data[0].length; j++) {
                total_group[t_ind] = n_data[i][j];
                t_ind++;
            }
        }
        
        float total_mean = GetMean(total_group);
        
        //k - 1 degrees of freedom
        float df1 = (n_data.length - 1);
        
        //N - k degrees of freedom
        float df2 = (n_data.length*n_data[0].length) - n_data.length;
        
        //Total sum of squares
        float SST = 0;
        for (int i = 0; i < total_group.length; i++) {
            SST += Math.pow(total_group[i]-total_mean,2);
        }
        
        //Sum of squares within groups
        float SSW = 0;
        for (int g = 0; g < n_data.length; g++) {
            for (int i = 0; i < n_data[0].length; i++) {
                SSW += Math.pow(n_data[g][i] - abs_group_means[g],2);
            }
        }
        
        //Sum of squares between groups
        float SSB = SST - SSW;
        
        //return F statistic, df1 and df2
        float varData[] = new float[3];
        varData[0] = (SSB / df1) / (SSW / df2);
        varData[1] = df1;
        varData[2] = df2;
        return varData;
}
         
         public float[] NormalityTest(String[] getVars) {
        String[][] data = new String[getVars.length][1];
        
        for (int i = 0; i < data.length; i++) {
            data[i] = ArrayMaps.columnData.get(getVars[i]);
        }
        
        float n_data[][] = new float[data.length][data[0].length-1];
        
        //convert data to float
        try {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length-1; j++) {
                    n_data[i][j] = Float.parseFloat(data[i][j+1]);
                }
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Some of your data may not be converted to numeric values.");
        }

        //Sort each data set
        for (int i = 0; i < n_data.length; i++) {
            //NOTE: This is independent sorting, so other columns WON'T affect others
            Arrays.sort(n_data[i]);
        }
        
        //Create empirical distribution for each data set
        float[] empirical = new float[n_data[0].length];
        
        for (int i = 0; i < n_data[0].length; i++) {
                empirical[i] = (float) (i)/n_data[0].length;
        }
            
        //Get means and sd for each data set
        float[] means = new float[n_data.length];
        float[] sds = new float[n_data.length];
        
        for (int i = 0; i < data.length; i++) {
            means[i] = GetMean(n_data[i]);
            sds[i] = GetStDev(n_data[i]);
        }
        
        //Cululative normal distribution value comparison
        float[][] c_norm_diff = new float[n_data.length][n_data[0].length];
        
        for (int c = 0; c < n_data.length; c++) {
            for (int r = 0; r < n_data[0].length; r++) {
                if (n_data[c][r] < means[c]) {
                    c_norm_diff[c][r] = Math.abs(empirical[r] - (1 - CumulativeNormal(n_data[c][r], means[c], sds[c])));
                } else {
                    c_norm_diff[c][r] = Math.abs(empirical[r] - CumulativeNormal(n_data[c][r], means[c], sds[c]));
                }
            }
        }
        
        //Sort the difference arrays
        for (int i = 0; i < c_norm_diff.length; i++) {
            Arrays.sort(c_norm_diff[i]);
        }
        
        //Get test statistics for each group
        float[] group_test_stats = new float[c_norm_diff.length];
        for (int i = 0; i < group_test_stats.length; i++) {
            group_test_stats[i] = c_norm_diff[i][n_data[0].length-1];
        }

        return group_test_stats;
     }
          //EOF
}
