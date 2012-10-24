package jcriteria.framework;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stories.Example;

public class StoryTest {

    private Story story;

    @Before
    public void setUp() throws Exception {
        story = Story.initFromTestClass(Example.class);
    }

    @Test
    public void shouldInitializeFromFile() {
        Assert.assertEquals("As a Product Owner I must write acceptance criteria", story.getTitle());
        Assert.assertTrue(story.getCriteria().size() > 0);
    }

    @Test
    public void testContainsCriteriaForMethodName() {
        Assert.assertTrue(story.containsCriteriaFor("Should run criteria"));
        Assert.assertFalse(story.containsCriteriaFor("Should not run criteria"));
    }
}
