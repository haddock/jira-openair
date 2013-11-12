package ut.se.valtech.jira.plugins;

import org.junit.Test;
import se.valtech.jira.plugins.MyPluginComponent;
import se.valtech.jira.plugins.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}