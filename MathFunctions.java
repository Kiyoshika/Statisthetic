
import java.util.Vector;
import javax.swing.JOptionPane;


public class MathFunctions {
    public float Mean(String getVars) {
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
        
        float sum = 0;
            for (int i = 0; i < input.length; i++) {
                sum += values[i];
            }
        return sum/input.length;
}
}
