package ConceptGraph;

import com.sun.javaws.exceptions.InvalidArgumentException;

/**
 * Created by Dan on 18/11/2015.
 */
public class SizedMaxMap {
    public final int maxSize;
    private double[] keys;
    private Object[] values;

    public SizedMaxMap(int maxSize) throws InvalidArgumentException {
        if(maxSize == 0){
            throw new InvalidArgumentException(new String[]{"maxSize must be greater than 0"});
        }
        this.maxSize = maxSize;
        keys = new double[maxSize];
        values = new Object[maxSize];
    }

    public boolean insert(double key, Object value){
        if(key < keys[maxSize -1]){
            return false;
        }
        for(int i = maxSize-1; i >= 0; i --){
            if(key < keys[i]){
                for(int j = maxSize - 1; j >= i + 2; j--){
                    keys[j] = keys[j - 1];
                    values[j] = values[j - 1];
                }
                keys[i + 1] = key;
                values[i+1] = value;
                return true;
            }
        }

        for(int j = maxSize - 1; j >= 1; j--){
            keys[j] = keys[j - 1];
            values[j] = values[j - 1];
        }
        keys[0] = key;
        values[0] = value;
        return true;
    }

    public double[] getKeys() {
        return keys;
    }

    public Object[] getValues() {
        return values;
    }
}
