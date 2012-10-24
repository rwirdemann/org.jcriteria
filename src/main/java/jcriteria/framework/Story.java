package jcriteria.framework;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Story {
    public static final String STORY_FILE_DIRECTORY = "src/test/resources/stories/";

    private String title;
    private List<String> criteria = new ArrayList<String>();

    public static Story initFromTestClass(Class<?> testClass) {
        Story story = new Story();
        try {
            File file = new File(STORY_FILE_DIRECTORY + toFileName(testClass));
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Keyword.from(line).process(story, line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public void addCriteria(String c) {
        criteria.add(c);
    }

    public boolean containsCriteriaFor(String criteriaName) {
        for (String c : criteria) {
            if (c.equals(criteriaName)) {
                return true;
            }
        }
        return false;
    }

    private static String toFileName(Class<?> testClass) {
        String simpleClassName = testClass.getSimpleName();
        if (StringUtils.endsWith(simpleClassName, "Test"))
            simpleClassName = StringUtils.remove(simpleClassName, "Test");

        return (toTitleCase(simpleClassName) + ".story").toLowerCase();
    }

    private static String toTitleCase(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        StringBuilder result = new StringBuilder();

        /*
         * Pretend space before first character
         */
        char prevChar = ' ';

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (prevChar == ' ' || prevChar == '_') {
                result.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c) && !Character.isUpperCase(prevChar)) {
                /*
                 * Insert space before start of word if camel case
                 */
                result.append('_');
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }

            prevChar = c;
        }

        return result.toString();
    }
}
