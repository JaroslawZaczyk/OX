package com.js.tictactoe.board.game;

import com.js.tictactoe.board.Board;
import com.js.tictactoe.board.coords.Coordinates;
import com.js.tictactoe.exceptions.WrongIndexException;
import com.js.tictactoe.judge.Judge;
import com.js.tictactoe.parser.DigitParser;
import com.js.tictactoe.parser.InputParser;
import com.js.tictactoe.player.Player;
import com.js.tictactoe.player.PlayersGenerator;

import java.util.List;
import java.util.Scanner;

public class Game {

    private Board board;
    private Match match;

    private Scanner scanner = new Scanner(System.in);
    private Player currentPlayer;


    private List<Player> players;

    public Game(Board board) {
        this.board = board;
    }

    public void runGame() {
        players = PlayersGenerator.createPlayers();
        match = new Match();
        match.setPlayers(players);

        currentPlayer = players.get(1);

        Judge judge = new Judge(board, board.getWidth() > board.getHeight() ? board.getHeight() : board.getWidth());
        boolean isWinner;
        int i = 0;
        do {
            switchPlayers();
            board.printBoard();
            boolean added;
            do {
                System.out.println("Player " + currentPlayer.getName() +
                        " make your move [pattern: x y] and x must be lower than " + board.getWidth() + ", y lower than " + board.getHeight());
                String line = DigitParser.correctCoordinates(scanner::nextLine);
                added = makeMove(line, currentPlayer);
            } while (!added);

            isWinner = judge.isWinner(currentPlayer.getSign());


            i++;

        } while (!isWinner && i < board.getHeight() * board.getWidth());


        if (isWinner) {
            match.addGameWinner(currentPlayer);
        } else {
            match.addGameDraw();
        }

        board.printBoard();
        System.out.println(match.getPlayerWithMorePoints().getName());
    }

    private void switchPlayers() {

        if (currentPlayer.equals(players.get(0))) {
            currentPlayer = players.get(1);
        } else {
            currentPlayer = players.get(0);
        }
    }


    private boolean makeMove(String line, Player currentPlayer) {
        try {
            int[] coords = InputParser.parseStringInput(line);
            return board.insertSign(Coordinates.parseCoordinates(coords), currentPlayer.getSign());
        } catch (WrongIndexException | NumberFormatException e) {
            System.out.println("Wrong coordinates, please try again.");
            line = scanner.nextLine();
            makeMove(line, currentPlayer);
        }
        return true;
    }


}
