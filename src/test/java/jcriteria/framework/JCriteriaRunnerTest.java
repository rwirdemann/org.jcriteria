package jcriteria.framework;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import stories.Empty;
import stories.Example;

import java.util.List;

public class JCriteriaRunnerTest {
    private JCriteriaRunner runner;

    @Before
    public void setup() throws InitializationError {
        runner = new JCriteriaRunner(Example.class);
    }

    @Test
    public void shouldRunCriteriaMethodIfContainedInStoryFile() {
        List<FrameworkMethod> testMethods = runner.computeTestMethods();
        Assert.assertEquals(2, testMethods.size());
        FrameworkMethod method = testMethods.get(0);
        Assert.assertEquals("shouldRunCriteria", method.getName());
    }

    @Test
    public void shouldSurpressNoNoRunnableMethodsException() {
        try {
            new JCriteriaRunner(Empty.class);
        } catch (InitializationError initializationError) {
            Assert.fail("Caught unexpected InitializationError");
        }
    }
}
