package ConceptGraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Dan on 18/11/2015.
 */
public class frqWriterTest extends TestCase {
    public frqWriterTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( frqWriterTest.class );
    }

    public void test_givenFileWriter_DoesntCrash() throws IOException {
        OutputStream outputStream = Mockito.mock(OutputStream.class);
        FrqWriter frqWriter = new FrqWriter(outputStream);
    }

}
