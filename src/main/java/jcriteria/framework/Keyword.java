package jcriteria.framework;

import java.util.Hashtable;
import java.util.Map;

public enum Keyword {

    TITLE {
        @Override
        public void process(Story story, String line) {
            story.setTitle(stripLeadingKeywordFrom(line));
        }
    },

    CRITERIA {
        @Override
        public void process(Story story, String line) {
            story.addCriteria(stripLeadingKeywordFrom(line));
        }
    },

    EMPTY {
        @Override
        public void process(Story story, String line) {
        }
    };

    private static String stripLeadingKeywordFrom(String line) {
        return line.trim().substring(line.trim().indexOf(":") + 1).trim();
    }

    public abstract void process(Story story, String line);

    public static Keyword from(String line) {
        return KEYWORDS.get(extractKeywordFrom(line));
    }

    private static String extractKeywordFrom(String line) {
        if (line.contains(":")) {
            return line.trim().substring(0, line.trim().indexOf(":"));
        }
        return "";
    }

    private static Map<String, Keyword> KEYWORDS = new Hashtable<String, Keyword>() {{
        put("Title", TITLE);
        put("Criteria", CRITERIA);
        put("", EMPTY);
    }};
}
