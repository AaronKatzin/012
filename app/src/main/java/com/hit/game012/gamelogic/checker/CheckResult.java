package com.hit.game012.gamelogic.checker;

import com.hit.game012.gamelogic.game.Index;

import java.util.List;

/**
 * An object containing information about rules violation.
 */

public class CheckResult {
    public enum State {SATISFIED, VIOLATES}
    private String message;
    private List<Index> errorIndexes;

    public CheckResult(State resultState, String message, List<Index> errorIndexes) {
        this.resultState = resultState;
        this.message = message;
        this.errorIndexes = errorIndexes;
    }

    private State resultState;

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Index> getErrorIndexes() {
        return errorIndexes;
    }

    public void setErrorIndexes(List<Index> errorIndexes) {
        this.errorIndexes = errorIndexes;
    }
}
