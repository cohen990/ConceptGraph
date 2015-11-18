package ConceptGraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Dan on 18/11/2015.
 */
public class SizedMaxArrayTest extends TestCase {
    public SizedMaxArrayTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

//    @Test(expected=IndexOutOfBoundsException.class)
    public void givenZero_ThrowsInvalidArgumentException()
    {
        SizedMaxArray arr = new SizedMaxArray(0);
    }
}
