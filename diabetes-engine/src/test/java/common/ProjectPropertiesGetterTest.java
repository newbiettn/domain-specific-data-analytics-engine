package common;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test unit for {@link common.ProjectPropertiesGetter}
 *
 * @author newbiettn on 6/3/18
 * @project diabetes-engine
 */

public class ProjectPropertiesGetterTest {
    private ProjectPropertiesGetter propGetter;

    @Before
    public void setUp () throws IOException {
        propGetter = ProjectPropertiesGetter.getSingleton();
    }

    @Test
    public void testGetProperty(){
        String str = propGetter.getProperty("for.test.unit.only");
        assertEquals("this is for test unit", str);
        assertNotEquals("this is incorrect", str);

    }
}
