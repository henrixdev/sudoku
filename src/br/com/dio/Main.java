package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.*;
import java.util.stream.Stream;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static Board board;
    private static final int BOARD_SIZE = 9;

    private static final String POSITIONS_STRING =
            "0,0;4,false 1,0;7,false 2,0;9,true 3,0;5,false 4,0;8,true 5,0;6,true 6,0;2,true 7,0;3,false 8,0;1,false " +
                    "0,1;1,false 1,1;3,true 2,1;5,false 3,1;4,false 4,1;7,true 5,1;2,false 6,1;8,false 7,1;9,true 8,1;6,true " +
                    "0,2;2,false 1,2;6,true 2,2;8,false 3,2;9,false 4,2;1,true 5,2;3,false 6,2;7,false 7,2;4,false 8,2;5,true " +
                    "0,3;5,true 1,3;1,false 2,3;3,true 3,3;7,false 4,3;6,false 5,3;4,false 6,3;9,false 7,3;8,true 8,3;2,false " +
                    "0,4;8,false 1,4;9,true 2,4;7,false 3,4;1,true 4,4;2,true 5,4;5,true 6,4;3,false 7,4;6,true 8,4;4,false " +
                    "0,5;6,false 1,5;4,true 2,5;2,false 3,5;3,false 4,5;9,false 5,5;8,false 6,5;1,true 7,5;5,false 8,5;7,true " +
                    "0,6;7,true 1,6;5,false 2,6;4,false 3,6;2,false 4,6;3,true 5,6;9,false 6,6;6,false 7,6;1,true 8,6;8,false " +
                    "0,7;9,true 1,7;8,true 2,7;1,false 3,7;6,false 4,7;4,true 5,7;7,false 6,7;5,false 7,7;2,true 8,7;3,false " +
                    "0,8;3,false 1,8;2,false 2,8;6,true 3,8;8,true 4,8;5,true 5,8;1,false 6,8;4,true 7,8;7,false 8,8;9,false";

    public static void main(String[] args) {
        Map<String, String> positions = parsePositions(POSITIONS_STRING);
        runMenu(positions);
    }

    private static void runMenu(Map<String, String> positions) {
        int option;
        do {
            printMenu();
            option = readNumber("Selecione uma opção: ", 1, 8);
            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> modifyNumber(false);
                case 3 -> modifyNumber(true);
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.out.println("Saindo do jogo...");
            }
        } while (option != 8);
    }

    private static void printMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1 - Iniciar um novo jogo");
        System.out.println("2 - Colocar um novo número");
        System.out.println("3 - Remover um número");
        System.out.println("4 - Visualizar jogo atual");
        System.out.println("5 - Verificar status do jogo");
        System.out.println("6 - Limpar jogo");
        System.out.println("7 - Finalizar jogo");
        System.out.println("8 - Sair");
    }

    private static Map<String, String> parsePositions(String positionsString) {
        return Stream.of(positionsString.split(" "))
                .collect(HashMap::new,
                        (map, s) -> {
                            String[] parts = s.split(";");
                            map.put(parts[0], parts[1]);
                        },
                        HashMap::putAll);
    }

    private static void startGame(Map<String, String> positions) {
        if (board != null) {
            System.out.println("O jogo já foi iniciado!");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            spaces.add(new ArrayList<>());
            for (int col = 0; col < BOARD_SIZE; col++) {
                String pos = positions.getOrDefault("%d,%d".formatted(row, col), "0,false");
                String[] parts = pos.split(",");
                int expected = Integer.parseInt(parts[0]);
                boolean fixed = Boolean.parseBoolean(parts[1]);
                spaces.get(row).add(new Space(expected, fixed));
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar!");
    }

    private static void modifyNumber(boolean remove) {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        int row = readNumber("Informe a linha (0-8): ", 0, 8);
        int col = readNumber("Informe a coluna (0-8): ", 0, 8);

        if (remove) {
            if (!board.clearValue(col, row)) {
                System.out.printf("A posição [%d,%d] tem valor fixo e não pode ser removida.\n", col, row);
            }
        } else {
            int value = readNumber("Informe o número a inserir (1-9): ", 1, 9);
            if (!board.changeValue(col, row, value)) {
                System.out.printf("A posição [%d,%d] tem valor fixo e não pode ser alterada.\n", col, row);
            }
        }
    }

    private static void showCurrentGame() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }

        Object[] args = new Object[81];
        int argPos = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Space space = board.getSpaces().get(col).get(row);
                args[argPos++] = " " + (space.getActual() == null ? " " : space.getActual());
            }
        }

        System.out.println("=== Tabuleiro Atual ===");
        System.out.printf(BoardTemplate.BOARD_TEMPLATE + "\n", args);
    }

    private static void showGameStatus() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }
        System.out.printf("Status atual: %s\n", board.getStatus().getLabel());
        System.out.println(board.hasErrors() ? "O jogo contém erros." : "O jogo não contém erros.");
    }

    private static void clearGame() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }
        System.out.println("Deseja realmente limpar o jogo? (sim/não)");
        String confirm = SCANNER.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")) {
            System.out.println("Informe 'sim' ou 'não'");
            confirm = SCANNER.next();
        }
        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("O jogo foi limpo.");
        }
    }

    private static void finishGame() {
        if (board == null) {
            System.out.println("O jogo ainda não foi iniciado!");
            return;
        }
        if (board.gameIsFinished()) {
            System.out.println("Parabéns! Você concluiu o jogo:");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("O jogo contém erros. Ajuste o tabuleiro antes de finalizar.");
        } else {
            System.out.println("O jogo não está completo. Preencha todos os espaços.");
        }
    }

    private static int readNumber(String prompt, int min, int max) {
        System.out.println(prompt);
        int value = SCANNER.nextInt();
        while (value < min || value > max) {
            System.out.printf("Informe um número entre %d e %d\n", min, max);
            value = SCANNER.nextInt();
        }
        return value;
    }
}
