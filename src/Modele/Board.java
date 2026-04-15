package Modele;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public int row;
    public int col;
    public byte[][] board;
    public byte player;
    public byte playersNumber;
    public boolean paint;

    public Board(int row, int col){
        this.row = row;
        this.col = col;
        board = new byte[row][col];
        this.player = 1;
        paint = false;
    }

    public void addToken(int colNumber) {
        for (int r = row - 1; r >= 0; r--) {
            if (board[r][colNumber] == 0) {
                board[r][colNumber] = this.player;
                break;
            }
        }
    }


    public void addPaintToken(int rowNumber, int colNumber, byte color){
        board[rowNumber][colNumber] = color;
    }

    public int getRandomMove() {
        List<Integer> available = new ArrayList<>();
        for (int c = 0; c < col; c++) {
            if (board[0][c] == 0) { // colonne non pleine
                available.add(c);
            }
        }
        if (available.isEmpty()) return -1; // match nul
        Random rand = new Random();
        return available.get(rand.nextInt(available.size()));
    }

    public byte getValue(int r, int c){
        return board[r][c];
    }

    public byte checkWin(){
        //Horizontal
        System.out.println("checkwin");
        for(int r = 0; r < this.row; r++){
            for(int c = 0; c <= this.col-4; c++){
                if(board[r][c] == player
                && board[r][c+1] == player
                && board[r][c+2] == player
                && board[r][c+3] == player){
                    for(int w = c; w < col && board[r][w] == player; w++){
                        board[r][w] = 3;
                    }
                    System.out.println("Horizontal");
                    return player;
                }
            }
        }
        //Vertical
        for(int c = 0; c < this.col; c++){
            for(int r = 0; r <= this.row-4; r++){
                if(board[r][c] == player
                        && board[r+1][c] == player
                        && board[r+2][c] == player
                        && board[r+3][c] == player){
                    for(int w = r; w < row && board[w][c] == player; w++){
                        board[w][c] = 3;
                        System.out.println(w);
                    }
                    System.out.println("Vertical");
                    return player;
                }
            }
        }
        //Diag. droite "/"
        for(int r = 0; r <= this.row-4; r++){
            for(int c = 3; c < this.col; c++){
                if(board[r][c] == player
                        && board[r+1][c-1] == player
                        && board[r+2][c-2] == player
                        && board[r+3][c-3] == player){
                    int wr = r; int wc = c;
                    while(wr < row && wc < col && board[wr][wc] == player){
                        board[wr][wc] = 3;
                        wr++; wc--;
                    }
                    System.out.println("diagd");
                    return player;
                }
            }
        }
        //Diag. gauche "\"
        for(int r = 0; r <= this.row-4; r++){
            for(int c = 0; c <= this.col-4; c++){
                if(board[r][c] == player
                        && board[r+1][c+1] == player
                        && board[r+2][c+2] == player
                        && board[r+3][c+3] == player){
                    int wr = r; int wc = c;
                    while(wr < row && wc < col && board[wr][wc] == player){
                        board[wr][wc] = 3;
                        wr++; wc++;
                    }
                    System.out.println("diagg");
                    return player;
                }
            }
        }
        return 0;
    }

    public byte getPlayersNumber(){
        return playersNumber;
    }

    public void setPlayersNumber(byte pn){
        this.playersNumber = pn;
    }


    private boolean isValidMove(int c) {
        return c >= 0 && c < this.col && this.board[0][c] == 0;
    }

    private boolean isBoardFull() {
        for (int c = 0; c < this.col; c++) {
            if (this.board[0][c] == 0) return false;
        }
        return true;
    }

    private int makeTempMove(int c, byte player) {
        for (int r = this.row - 1; r >= 0; r--) {
            if (this.board[r][c] == 0) {
                this.board[r][c] = player;
                return r;
            }
        }
        return -1;
    }

    private void undoTempMove(int r, int c, byte player) {
        this.board[r][c] = 0;
    }

    private byte checkWinFrom(int row, int col) {
        byte p = this.board[row][col];
        if (p == 0 || p == 3) return 0;

        int[][] dirs = {{0,1},{1,0},{1,1},{1,-1}};
        for (int[] d : dirs) {
            int count = countDir(row, col, d[0], d[1], p) +
                    countDir(row, col, -d[0], -d[1], p) - 1;
            if (count >= 4) return p;
        }
        return 0;
    }

    private int countDir(int r, int c, int dr, int dc, byte p) {
        int count = 1;
        int nr = r + dr, nc = c + dc;
        while (nr >= 0 && nr < this.row && nc >= 0 && nc < this.col &&
                this.board[nr][nc] == p) {
            count++;
            nr += dr; nc += dc;
        }
        return count;
    }

    public int getBestMove(int depth) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = this.col / 2;
        byte opponent = (byte)(this.player == 1 ? 2 : 1);

        // 1. Victoire immédiate
        for (int c = 0; c < this.col; c++) {
            if (isValidMove(c)) {
                int rowPlaced = makeTempMove(c, this.player);
                if (checkWinFrom(rowPlaced, c) == this.player) {
                    undoTempMove(rowPlaced, c, this.player);
                    return c;
                }
                undoTempMove(rowPlaced, c, this.player);
            }
        }

        // 2. Blocage adversaire
        for (int c = 0; c < this.col; c++) {
            if (isValidMove(c)) {
                int rowPlaced = makeTempMove(c, opponent);
                if (checkWinFrom(rowPlaced, c) == opponent) {
                    undoTempMove(rowPlaced, c, opponent);
                    return c;
                }
                undoTempMove(rowPlaced, c, opponent);
            }
        }

        //minimax
        for (int c = 0; c < this.col; c++) {
            if (isValidMove(c)) {
                int rowPlaced = makeTempMove(c, this.player);
                int score = minimax(rowPlaced, c, depth-1, Integer.MIN_VALUE,
                        Integer.MAX_VALUE, false, this.player);
                undoTempMove(rowPlaced, c, this.player);
                System.out.printf("Col %d: score=%d\n", c, score);
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = c;
                }
            }
        }
        return bestCol;
    }

    private int minimax(int lastRow, int lastCol, int depth, int alpha, int beta,
                        boolean isMaxPlayer, byte aiPlayer) {
        byte winner = checkWinFrom(lastRow, lastCol);
        if (winner != 0) {
            return (winner == aiPlayer) ? 10000-depth*100 : depth*100-10000;
        }
        if (depth == 0 || isBoardFull()) {
            return evaluateBoard(aiPlayer);
        }

        byte currentPlayer = isMaxPlayer ? aiPlayer : (byte)(aiPlayer == 1 ? 2 : 1);

        if (isMaxPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int c = 0; c < this.col; c++) {
                if (isValidMove(c)) {
                    int r = makeTempMove(c, currentPlayer);
                    int score = minimax(r, c, depth-1, alpha, beta, false, aiPlayer);
                    undoTempMove(r, c, currentPlayer);
                    maxEval = Math.max(maxEval, score);
                    alpha = Math.max(alpha, score);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int c = 0; c < this.col; c++) {
                if (isValidMove(c)) {
                    int r = makeTempMove(c, currentPlayer);
                    int score = minimax(r, c, depth-1, alpha, beta, true, aiPlayer);
                    undoTempMove(r, c, currentPlayer);
                    minEval = Math.min(minEval, score);
                    beta = Math.min(beta, score);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    private int evaluateBoard(byte aiPlayer) {
        byte opp = (byte)(aiPlayer == 1 ? 2 : 1);
        return countAlignments(3, aiPlayer)*5000 - countAlignments(3, opp)*5000 +
                countAlignments(2, aiPlayer)*200 - countAlignments(2, opp)*200 +
                getCenterScore(aiPlayer)*50;
    }

    private int countAlignments(int len, byte player) {
        int count = 0;
        // Horizontal
        for (int r = 0; r < this.row; r++)
            for (int c = 0; c <= this.col-len; c++)
                if (checkLine(r, c, 0, 1, len, player)) count++;
        // Vertical
        for (int c = 0; c < this.col; c++)
            for (int r = 0; r <= this.row-len; r++)
                if (checkLine(r, c, 1, 0, len, player)) count++;
        return count;
    }

    private boolean checkLine(int r, int c, int dr, int dc, int len, byte player) {
        for (int i = 0; i < len; i++) {
            int nr = r + i*dr, nc = c + i*dc;
            if (nr < 0 || nr >= this.row || nc < 0 || nc >= this.col ||
                    this.board[nr][nc] != player)
                return false;
        }
        return true;
    }

    private int getCenterScore(byte player) {
        int score = 0, center = this.col/2;
        for (int r = 0; r < this.row; r++)
            if (this.board[r][center] == player) score++;
        return score;
    }






}
