package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Handler handler = new Handler();

    private TextView playerOneScore, playerTwoScore;
    private Button [] buttons = new Button[9];
    private Button resetGame, btnBackToMainMenu;

    private AppCompatImageView playerOneImage, playerTwoImage;

    private int playerOneScoreCount, playerTwoScoreCount, roundCount;
    boolean playerOneTurn;
    boolean boardNotReady;

    //p1 => 0;
    //p2 => 1;
    //empty => 2;
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    int [][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
            {0, 4, 8}, {2, 4, 6} // diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap imgPlayerOne = (Bitmap) getIntent().getParcelableExtra("imgPlayerOne");
        Bitmap imgPlayerTwo = (Bitmap) getIntent().getParcelableExtra("imgPlayerTwo");

        playerOneImage = (AppCompatImageView) findViewById(R.id.playerOneImage);
        playerTwoImage = (AppCompatImageView) findViewById(R.id.playerTwoImage);

        playerOneImage.setImageBitmap(imgPlayerOne);
        playerTwoImage.setImageBitmap(imgPlayerTwo);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);

        resetGame = (Button) findViewById(R.id.btnResetGame);
        btnBackToMainMenu = (Button) findViewById(R.id.btnBackToMainMenu);

        boardNotReady = false;

        for(int i = 0; i < buttons.length; i++){
            String buttonId = "btn" + i;
            int resourceId = getResources().getIdentifier(buttonId,  "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceId);
            buttons[i].setOnClickListener(this);
        }

        resetGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                updatePlayerScore();
                playAgain();
            }
        });

        btnBackToMainMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            }
        });

        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        playerOneTurn = true;
    }

    @Override
    public void onClick(View view) {
        if(boardNotReady)
            return;

        if(!(
                ((Button)view).getText().toString().equals("")
        )){
            return;
        }

        String buttonId = view.getResources().getResourceEntryName(view.getId());
        int buttonIdLength = buttonId.length();
        int gameStatePointer = Integer.parseInt(buttonId.substring(buttonIdLength -1, buttonIdLength));

        if(playerOneTurn){
            ((Button)view).setText("X");
            ((Button)view).setTextColor(Color.parseColor("#E19E20"));
            gameState[gameStatePointer] = 0;
        }else{
            ((Button)view).setText("O");
            ((Button)view).setTextColor(Color.parseColor("#BD6193"));
            gameState[gameStatePointer] = 1;
        }

        roundCount++;


        if(checkWinner()){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wonMessage();
                    updatePlayerScore();
                    playAgain();
                }
            }, 2000);
            return;
        }
        if(roundCount == 9){
            Toast.makeText(this, "No winner!", Toast.LENGTH_SHORT).show();
            playAgain();
            return;
        }

        changePlayerTurn();
    }

    public void changePlayerTurn(){
        playerOneTurn = !playerOneTurn;
    }

    public boolean checkWinner(){
        for(int [] winningPosition: winningPositions){
            if(
                    gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                            gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                            gameState[winningPosition[0]] != 2
            ){
                highlightWinMove(winningPosition[0], winningPosition[1], winningPosition[2]);
                boardNotReady = true;
                return true;
            }
        }
        return false;
    }

    public void highlightWinMove(int firstPosition, int secondPosition, int thirdPosition){
        buttons[firstPosition].setTextColor(Color.parseColor("#38C190"));
        buttons[secondPosition].setTextColor(Color.parseColor("#38C190"));
        buttons[thirdPosition].setTextColor(Color.parseColor("#38C190"));
    }

    public void playAgain(){
        roundCount = 0;
        playerOneTurn = true;

        for(int i = 0; i < buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }

        boardNotReady = false;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void wonMessage(){
        String wonText;

        if(playerOneTurn) {
            playerOneScoreCount++;
            wonText = "Player one won!";
        }else {
            playerTwoScoreCount++;
            wonText = "Player two won!";
        }

        Toast.makeText(this, wonText, Toast.LENGTH_SHORT).show();
    }
}