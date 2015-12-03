package ConceptGraph;

import ConceptGraph.Output.FileOutputAssistant;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class FileOutputAssistantTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FileOutputAssistantTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FileOutputAssistantTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void getWriterForGraph_GivenEnormousFileName_DoesntThrow()
    {
        FileOutputAssistant output = new FileOutputAssistant();
        Exception exception = null;
        try {
                output.getWriterForGraph("aluwysgfliasbdfgluiashlihbnqweASUKLGLAUSDFGHBNLUIAERHOUHASI;DGHLAISHGLAISGHLASthe#0x20master0x20d#20wardens#0x20and#0x20brethren#0x20and#0x20sisters#0x20of#0x20the#0x20guild#0x20or#0x20fraternity#0x20of#0x20the#0x20blessed#0x20mary#0x20tluaojwysdg'flaysdgh'bfa;ilsdgh'fl;ai'sdghhe#0x20virgin#0x20of#0x20the#0x20mystery#0x20of#0x20drapers#0x20of#0x20the#0x20city#0x20of#0", false);
        } catch (IOException e) {
            exception = e;
        }

        assertNull(exception);
    }
}
