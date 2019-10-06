package com.example.sliderpuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class SliderActivity extends AppCompatActivity
{
    int[] gameArray;
    RelativeLayout layout;

    //Assorted variables used for defining game board and pieces.
    int boardSize = 4;
    int offset = 10;

    int screenWidth;
    int screenHeight;
    int initialX;
    int initialY;
    int boardWidth;
    int boardHeight;
    int tileWidth;
    int tileHeight;
    int mainBar;
    int returnHome;
    int statsDisplay;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //Hides title bar
        setContentView(R.layout.activity_slider);

        //Get the layout
        layout = findViewById(R.id.gameLayout);

        //Get the size of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Defining various variables for size of game board and tiles, and starting points.
        mainBar = (int)(screenWidth * 0.2);

        initialX = (int)(screenWidth * 0.025);
        initialY = (int)(screenHeight * 0.025) + mainBar;

        boardWidth = (int)(screenWidth * 0.95);
        boardHeight = ((int)(screenHeight * 0.95)) - mainBar - 50;

        tileWidth = (boardWidth / boardSize);
        tileHeight = (boardHeight / boardSize);


        //Sizes for views on the Main Bar
        returnHome = screenWidth / 10;
        statsDisplay = screenWidth / 5;

        //Start a game
        newGame();
    }

    //Starts a new game.
    void newGame()
    {
        //Initiate game board array
        gameArray = new int[boardSize * boardSize];
        for(int i = 0; i < boardSize * boardSize; i++)
        {
            gameArray[i] = i;
        }
        shuffleArray(gameArray);

        drawBoard();
    }

    //Function to draw the game board
    private void drawBoard()
    {
        //Clears the screen layout before drawing
        //TODO Erase when tiles get animated, will no longer have to continuously redraw screen.
        layout.removeAllViews();

        //Draws the top bar
        drawHomeBar();

        //Create game board
        for(int i = 0; i < gameArray.length; i++)
        {
            if(gameArray[i] != 0)
            {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.round_corners);
                tv.setId(gameArray[i]);
                tv.setText(String.valueOf(gameArray[i]));
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(Color.BLUE);
                tv.setTextColor(Color.WHITE);

                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tileHeight / 3);


                tv.setWidth(tileWidth - (offset * 2));
                tv.setHeight(tileHeight - (offset * 2));

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMove(v);
                    }
                });

                float xCoord = (float) ((initialX + (tileWidth * (i % boardSize))) + offset);
                float yCoord = (float) ((initialY + (tileHeight * (i / boardSize))) + offset);

                tv.setX(xCoord);
                tv.setY(yCoord);

                layout.addView(tv);
            }
        }
    }

    void drawHomeBar()
    {
        //Blank textview used for bar background
        TextView tvBackground = new TextView(this);
        tvBackground.setWidth(screenWidth);
        tvBackground.setHeight(mainBar);
        tvBackground.setX(0); tvBackground.setY(0);
        tvBackground.setBackgroundColor(Color.DKGRAY);

        //Return to menu button
        TextView tvGoHome = new TextView(this);
        tvGoHome.setText("X");
        tvGoHome.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainBar / 3);
        tvGoHome.setBackgroundColor(Color.WHITE);
        tvGoHome.setWidth(returnHome - (offset * 2));
        tvGoHome.setHeight(mainBar - (offset * 2));
        tvGoHome.setX(initialX + offset);
        tvGoHome.setY(offset);
        tvGoHome.setGravity(Gravity.CENTER);

        //Display for move counter
        TextView tvMoves = new TextView(this);
        tvMoves.setText("Moves: 00");
        tvMoves.setGravity(Gravity.CENTER);
        tvMoves.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainBar / 5);
        tvMoves.setBackgroundColor(Color.WHITE);
        tvMoves.setWidth(statsDisplay - (offset * 2));
        tvMoves.setHeight(mainBar - (offset * 2));
        tvMoves.setX((int)(screenWidth * 0.6) + offset  - (int)(screenWidth * 0.025));
        tvMoves.setY(offset);

        //Display for timer
        TextView tvTime = new TextView(this);
        tvTime.setText("00:00");
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainBar / 4);
        tvTime.setBackgroundColor(Color.WHITE);
        tvTime.setWidth(statsDisplay - (offset * 2));
        tvTime.setHeight(mainBar - (offset * 2));
        tvTime.setX((int)(screenWidth * 0.8) + offset - (int)(screenWidth * 0.025));
        tvTime.setY(offset);

        //Add views to the layout
        layout.addView(tvBackground);
        layout.addView(tvGoHome);
        layout.addView(tvMoves);
        layout.addView(tvTime);
    }

    public void updateMove(View tv)
    {
        int blankLocation = findIndexOf(gameArray, 0);
        int clickedLocation = findIndexOf(gameArray, tv.getId());
        int range = Math.abs(blankLocation - clickedLocation);

        if (range == boardSize || (range == 1 && (Math.abs((blankLocation % boardSize) - (clickedLocation % boardSize))) == 1))
        {
            swap(gameArray, blankLocation, clickedLocation);
            drawBoard();
        }

    }


    //Shuffles the given array
    void shuffleArray(int[] array)
    {
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < array.length; i++)
        {
            int change = i + random.nextInt(array.length - i);
            swap(array, i, change);
        }
    }

    //Helper function to get the index position of given number
    int findIndexOf(int[] array, int num)
    {
        for(int i = 0; i < array.length; i++)
        {
            if (array[i] == num)
                return i;
        }

        return -1;
    }

    //Helper function that swaps two values in an array
    void swap(int[] array, int a, int b)
    {
        int temp = array[a];
        array[b] = array[b];
        array[b] = temp;
    }
}
