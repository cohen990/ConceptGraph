package ConceptGraph;

import java.lang.reflect.Array;

/**
 * Created by Dan on 18/11/2015.
 */
public class SizedMaxArray {
    public final int maxSize;
    private int[] arr;

    public SizedMaxArray(int maxSize){
        this.maxSize = maxSize;
        arr = new int[maxSize];
    }

    public boolean insert(int toInsert){
        if(toInsert < arr[maxSize -1]){
            return false;
        }
        for(int i = maxSize-1; i >= 0; i --){
            if(toInsert < arr[i]){
                for(int j = i + 2; j < maxSize; j++){
                    arr[j] = arr[j - 1];
                }
                arr[i + 1] = toInsert;
                return true;
            }
        }

        for(int j = 1; j < maxSize; j++){
            arr[j] = arr[j - 1];
        }
        arr[0] = toInsert;
        return true;
    }
}
