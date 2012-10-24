package jcriteria.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the connection between a criteria in the story file and the belonging method in the
 * story class:
 *
 * Title: As a user I want to login
 * Criteria: User must provide his email
 *
 * @Criteria("User must provide his email")
 * public void emailMustBeGiven() {}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Criteria {
    String value();
}