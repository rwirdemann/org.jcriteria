package jcriteria.framework;

import org.apache.commons.lang.StringUtils;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JCriteriaRunner extends BlockJUnit4ClassRunner {
    public static PrintStream out = System.out;
    private Story story;
    private StoryReporter storyReporter;

    public JCriteriaRunner(Class<?> klass) throws InitializationError {
        this(klass, new StoryReporter());
    }

    JCriteriaRunner(Class<?> klass, StoryReporter storyReporter) throws InitializationError {
        super(klass);
        this.storyReporter = storyReporter;
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        final Statement next = super.classBlock(notifier);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                storyReporter.setTitle(getStory().getTitle());
                reportCriteriasWithoutTestMethods();
                next.evaluate();
                storyReporter.report();
            }
        };
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        removeNoRunnableMethodsExceptionFrom(errors);
    }

    private void removeNoRunnableMethodsExceptionFrom(List<Throwable> errors) {
        Iterator<Throwable> iterator = errors.iterator();
        while (iterator.hasNext()) {
            Throwable t = iterator.next();
            if ("No runnable methods".equals(t.getMessage())) {
                iterator.remove();
            }
        }
    }

    private void reportCriteriasWithoutTestMethods() {
        List<String> testMethodAnnotations = testMethodAnnotationsAsStringList();
        for (String criteria : getStory().getCriteria()) {
            if (!testMethodAnnotations.contains(criteria)) {
                reportCriteriaAsPending(criteria);
            }
        }
    }

    private void reportCriteriaAsPending(String criteria) {
        String pendingString = "  Criteria: " + criteria;
        pendingString = StringUtils.rightPad(pendingString, 100, ".");
        storyReporter.addCriteria(pendingString + "[PENDING]");
        out.flush();
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return removeTestMethodsWithoutCriterias();
    }

    private List<String> testMethodAnnotationsAsStringList() {
        List<FrameworkMethod> testMethods = getTestClass().getAnnotatedMethods(Criteria.class);
        List<String> result = new ArrayList<String>();
        for (FrameworkMethod testMethod : testMethods) {
            result.add(testMethod.getAnnotation(Criteria.class).value());
        }
        return result;
    }

    private List<FrameworkMethod> removeTestMethodsWithoutCriterias() {
        List<FrameworkMethod> testMethods = getTestClass().getAnnotatedMethods(Criteria.class);
        Iterator<FrameworkMethod> iterator = testMethods.iterator();
        while (iterator.hasNext()) {
            FrameworkMethod fm = iterator.next();
            Criteria c = fm.getAnnotation(Criteria.class);
            if (!getStory().containsCriteriaFor(c.value())) {
                iterator.remove();
            }
        }
        return testMethods;
    }

    private Story getStory() {
        if (story == null) {
            story = Story.initFromTestClass(getTestClass().getJavaClass());
        }
        return story;
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        return new CriteriaStatement(method, test);
    }

    private class CriteriaStatement extends Statement {

        private FrameworkMethod method;
        private Object test;

        private CriteriaStatement(FrameworkMethod method, Object test) {
            this.method = method;
            this.test = test;
        }

        @Override
        public void evaluate() throws Throwable {
            String criteria = "  Criteria: " + method.getAnnotation(Criteria.class).value();
            criteria = StringUtils.rightPad(criteria, 100, ".");
            try {
                method.invokeExplosively(test);
                storyReporter.addCriteria(criteria + "[OK]");
            } catch (Throwable throwable) {
                storyReporter.addCriteria(criteria + "[FAILED]", throwable);
            }
        }
    }
}
