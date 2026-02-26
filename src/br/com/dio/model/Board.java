package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    // Status do jogo
    public GameStatusEnum getStatus() {
        boolean anyFilled = spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.isFixed());

        if (!anyFilled) {
            return GameStatusEnum.NON_STARTED;
        }

        boolean anyEmpty = spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(Space::isEmpty);

        return anyEmpty ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;
    }

    // Checa se existe algum erro
    public boolean hasErrors() {
        if (getStatus() == GameStatusEnum.NON_STARTED) {
            return false;
        }
        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(Space::hasError);
    }

    // Alterar valor de uma célula
    public boolean changeValue(final int col, final int row, final int value) {
        Space space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    // Limpar valor de uma célula
    public boolean clearValue(final int col, final int row) {
        Space space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }
        space.clear();
        return true;
    }

    // Resetar todo o tabuleiro
    public void reset() {
        spaces.forEach(column -> column.forEach(Space::clear));
    }

    // Verifica se o jogo terminou corretamente
    public boolean gameIsFinished() {
        return getStatus() == GameStatusEnum.COMPLETE && !hasErrors();
    }
}
