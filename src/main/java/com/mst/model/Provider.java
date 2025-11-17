package com.mst.model;

public enum Provider {
    GITHUB("github"),
    JIRA("jira"),
    CLICKUP("clickup");

    private final String folderName;

    Provider(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public static Provider fromString(String text) {
        for (Provider provider : Provider.values()) {
            if (provider.folderName.equalsIgnoreCase(text)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("No provider with folder name " + text + " found");
    }
}