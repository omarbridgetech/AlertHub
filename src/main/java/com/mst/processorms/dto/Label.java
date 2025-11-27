package com.mst.processorms.dto;

public enum Label {


        BUG("bug"),
        DOCUMENTATION("documentation"),
        DUPLICATE("duplicate"),
        ENHANCEMENT("enhancement"),
        GOOD_FIRST_ISSUE("good_first_issue"),
        HELP_WANTED("help_wanted"),
        INVALID("invalid"),
        QUESTION("question"),
        WONTFIX("wontfix");

        private final String value;

        Label(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value; // כך מקבלים את המחרוזת לבסיס נתונים / Loader (lower_case עם underscores)
        }
}
