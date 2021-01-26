package com.rule.client;

public enum RiskCategory {

    /**
     * death risk
     */
    DEATH(1),

    /**
     * critical illness risk
     */
    CRITICAL_ILLNESS(2);

    private final Integer key;

    public Integer getKey() {
        return key;
    }

    RiskCategory(Integer key) {
        this.key = key;
    }
}
