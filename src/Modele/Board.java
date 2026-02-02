package Modele;
import java.util.Random;

public class Board {
    public int row;
    public int col;
    public byte[][] board;
    public byte player;
    public byte playersNumber;

    public Board(int row, int col){
        this.row = row;
        this.col = col;
        board = new byte[row][col];
        this.player = 1;
    }

    public void addToken(int colNumber){
        for(int r = row-1; r >= 0; r--){
            if(board[r][colNumber] == 0){
                board[r][colNumber] = this.player;
                this.player = (byte)(player == 1 ? 2 : 1);
                break;
            }
        }
        //Sinon renvoyer exception pas de place?
    }

    public void addTokenAlea(){
        Random r = new Random();
        addToken(r.nextInt(col));
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

}
