package com.automationexercise.framework.components;

/** The collapsible groups in the Category sidebar (anchors observed as #Women, #Men, #Kids). */
public enum CategoryGroup {
    WOMEN("#Women"),
    MEN("#Men"),
    KIDS("#Kids");

    private final String anchor;

    CategoryGroup(String anchor) {
        this.anchor = anchor;
    }

    public String anchor() {
        return anchor;
    }
}