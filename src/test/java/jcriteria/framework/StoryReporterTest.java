package jcriteria.framework;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import stories.Example;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StoryReporterTest {

    private StoryReporter reporter;
    private PrintStream output;

    @Before
    public void setUp() throws InitializationError {
        output = Mockito.mock(PrintStream.class);
        reporter = new StoryReporter(output);
        JCriteriaRunner runner = new JCriteriaRunner(Example.class, reporter);
        runner.run(new RunNotifier());
    }

    @Test
    public void shouldCaptureStoryTitle() {
        Assert.assertEquals("As a Product Owner I must write acceptance criteria", reporter.getTitle());
    }

    @Test
    public void shouldCollectCriteriaResults() {
        List<CriteriaResult> criteriaResults = reporter.getCriteriaResults();
        Assert.assertEquals(3, criteriaResults.size());
        String criteria = criteriaResults.get(0).getCriteria().trim();
        Assert.assertThat(criteria, new StartsWithMatcher("Criteria: Should report pending criteria"));
    }

    @Test
    public void shouldReportStoryTitle() {
        verify(output).println(eq("User Story: As a Product Owner I must write acceptance criteria"));
    }

    @Test
    public void shouldReportCriteria() {
        verify(output).println(startsWith("Criteria: Should run criteria"));
        verify(output).println(startsWith("Criteria: Should report pending criteria"));
        verify(output).println(startsWith("Criteria: Run and fail"));
    }

    @Test
    public void shouldReportFailedException() {
        List<CriteriaResult> failed = getFailedCriteriaResults();
        assertFalse(failed.isEmpty());
        verify(output).println(failed.get(0).getThrowable());
    }

    private List<CriteriaResult> getFailedCriteriaResults() {
        List<CriteriaResult> failed = new ArrayList<CriteriaResult>();
        for (CriteriaResult criteriaResult : reporter.getCriteriaResults())
            if (criteriaResult.getThrowable() != null)
                failed.add(criteriaResult);
        return failed;
    }

    @Test
    public void shouldReportSuccessfullCriteria() {
        verify(output, times(1)).println(startsAndEndsWith("Criteria: Should run criteria", "[OK]"));
    }

    @Test
    public void shouldReportFailedCriteria() {
        verify(output, times(1)).println(startsAndEndsWith("Criteria: Run and fail", "[FAILED]"));
    }

    @Test
    public void shouldReportPendingCriteria() {
        verify(output, times(1)).println(startsAndEndsWith("Criteria: Should report pending criteria", "[PENDING]"));
    }

    private String startsWith(final String expected) {
        return argThat(new StartsWithMatcher(expected));
    }

    private String startsAndEndsWith(final String expectedStart, final String expectedEnd) {
        return argThat(new StartsAndEndsWithMatcher(expectedStart, expectedEnd));
    }

    private class StartsWithMatcher extends ArgumentMatcher<String> {
        private String expected;

        StartsWithMatcher(String expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object o) {
            return ((String) o).trim().startsWith(expected.trim());
        }
    }

    private class StartsAndEndsWithMatcher extends ArgumentMatcher<String> {
        private String expectedStart;
        private String expectedEnd;

        StartsAndEndsWithMatcher(String expectedStart, String expectedEnd) {
            this.expectedStart = expectedStart;
            this.expectedEnd = expectedEnd;
        }

        @Override
        public boolean matches(Object o) {
            return ((String) o).trim().startsWith(expectedStart.trim())
                    && ((String) o).trim().endsWith(expectedEnd.trim());
        }
    }
}
