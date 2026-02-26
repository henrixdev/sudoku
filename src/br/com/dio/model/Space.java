package br.com.dio.model;

import java.util.Objects;

public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(final int expected, final boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed) {
            this.actual = expected;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(final Integer actual) {
        if (!fixed) {
            this.actual = actual;
        }
    }

    public void clear() {
        this.actual = null;
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public boolean hasError() {
        return Objects.nonNull(actual) && !actual.equals(expected);
    }

    public boolean isEmpty() {
        return actual == null;
    }
}
