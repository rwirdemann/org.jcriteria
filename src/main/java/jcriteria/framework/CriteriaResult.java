package jcriteria.framework;

public class CriteriaResult {
    
    private String criteria;
    private Throwable throwable;

    public CriteriaResult(String criteria) {
        this(criteria, null);
    }

    public CriteriaResult(String criteria, Throwable throwable) {
        this.criteria = criteria;
        this.throwable = throwable;
    }

    public String getCriteria() {
        return criteria;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
