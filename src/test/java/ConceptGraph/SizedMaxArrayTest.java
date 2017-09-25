package ConceptGraph;

import ConceptGraph.DataStructures.SizedMaxMap;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dan on 18/11/2015.
 */
public class SizedMaxArrayTest{
    @Test
    public void test_givenZero_ThrowsInvalidArgumentException() {
        try {
            SizedMaxMap arr = new SizedMaxMap(0);
        }
        catch(InvalidArgumentException e){
            assertFalse(e.equals(null));
            return;
        }

        // nothing was thrown
        assertTrue(false);
    }

    @Test
    public void test_givenTen_SetsTen() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(10);

        assertTrue(10 == arr.maxSize);
    }

    @Test
    public void test_arrSize1_GivenFirstNumber_ReturnsTrue() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        assertTrue(arr.insert(10, 0));
    }

    @Test
    public void test_arrSize1_GivenSecondNumberLarger_ReturnsTrue() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        assertTrue(arr.insert(11, "poop"));
    }

    @Test
    public void test_arrSize1_GivenSecondNumberLarger_SetsArrToLarger() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        arr.insert(11, 15);

        assertEquals(11.0, arr.getKeys()[0], 0);
    }

    @Test
    public void test_arrSize1_GivenSecondNumberLarger_SetsValToValOFLarger() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        arr.insert(11, 15);

        assertEquals(15, arr.getValues()[0]);
    }

    @Test
    public void test_arrSize1_GivenSecondNumberSmaller_ReturnsFalse() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        assertFalse(arr.insert(9, 10));
    }

    @Test
    public void test_arrSize1_GivenSecondNumberSmaller_KeepsArrAtLarger() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        assertFalse(arr.insert(9, 15));

        assertEquals(10.0, arr.getKeys()[0], 0);
    }

    @Test
    public void test_arrSize1_GivenSecondNumberSmaller_KeepsValuesAtValOfLarger() throws InvalidArgumentException {
        SizedMaxMap arr = new SizedMaxMap(1);

        arr.insert(10, 0);
        assertFalse(arr.insert(9, 15));

        assertEquals(0, arr.getValues()[0]);
    }

    @Test
    public void test_arrSize5_6Inserts_Keeps5Largest() throws InvalidArgumentException{
        SizedMaxMap arr = new SizedMaxMap(5);

        arr.insert(1, 5);
        arr.insert(10, 12);
        arr.insert(15, 1);
        arr.insert(6, 2);
        arr.insert(12, 19);
        arr.insert(9, 1);

        double[] keys = arr.getKeys();
        Object[] vals= arr.getValues();
        assertEquals(15.0, keys[0], 0);
        assertEquals(1, vals[0]);
        assertEquals(12.0, keys[1], 0);
        assertEquals(19, vals[1]);
        assertEquals(10.0, keys[2], 0);
        assertEquals(12, vals[2]);
        assertEquals(9.0, keys[3], 0);
        assertEquals(1, vals[3]);
        assertEquals(6.0, keys[4], 0);
        assertEquals(2, vals[4]);
    }
}
