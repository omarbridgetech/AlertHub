package com.mst.metricms.model;

public enum Label {
    BUG("bug", "Something isn't working"),
    DOCUMENTATION("documentation", "Improvements or additions to documentation"),
    DUPLICATE("duplicate", "This issue or pull request already exists"),
    ENHANCEMENT("enhancement", "New feature or request"),
    GOOD_FIRST_ISSUE("good_first_issue", "Good for newcomers"),
    HELP_WANTED("help_wanted", "Extra attention is needed"),
    INVALID("invalid", "This doesn't seem right"),
    QUESTION("question", "Further information is requested"),
    WONTFIX("wontfix", "This will not be worked on");

    private final String value;
    private final String description;

    Label(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get Label enum from string value (case-insensitive)
     */
    public static Label fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Label value cannot be null");
        }

        for (Label label : Label.values()) {
            if (label.value.equalsIgnoreCase(value) || label.name().equalsIgnoreCase(value)) {
                return label;
            }
        }

        throw new IllegalArgumentException("Invalid label: " + value +
                ". Valid labels are: bug, documentation, duplicate, enhancement, " +
                "good_first_issue, help_wanted, invalid, question, wontfix");
    }

    @Override
    public String toString() {
        return value;
    }
}
