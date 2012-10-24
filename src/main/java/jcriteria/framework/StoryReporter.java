package jcriteria.framework;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class StoryReporter {
    private String title;
    private List<CriteriaResult> criteriaResults;
    private final PrintStream output;

    public StoryReporter() {
        this(System.out);
    }

    public StoryReporter(PrintStream out) {
        criteriaResults = new ArrayList<CriteriaResult>();
        this.output = out;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void report() {
        output.println("User Story: " + getTitle());
        for (CriteriaResult c : criteriaResults) {
            output.println(c.getCriteria());
            if (c.getThrowable() != null)
                c.getThrowable().printStackTrace(output);
        }
    }

    public List<CriteriaResult> getCriteriaResults() {
        return criteriaResults;
    }

    public void addCriteria(String c) {
        criteriaResults.add(new CriteriaResult(c));
    }

    public void addCriteria(String c, Throwable t) {
        criteriaResults.add(new CriteriaResult(c, t));
    }
}
