package com.codewithgauresh.xo;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

/**
 * Main activity for the XO (Tic Tac Toe) game.
 * This class handles game logic, player turns, and UI updates.
 */
public class MainActivity extends AppCompatActivity {

    // 0: Player X (User A), 1: Player O (User B)
    private int activePlayer = 0;
    // Array representing the board state: 0 = X, 1 = O, 2 = Empty
    private final int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    // All possible winning combinations (rows, columns, diagonals)
    private final int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
    };
    // Flag to track if the game is still ongoing
    private boolean gameActive = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * Called when a player taps a square in the grid.
     * Handles move placement, turn switching, and game termination.
     */
    public void playertap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        // If game is over, any tap will reset the board
        if (!gameActive) {
            gameReset();
            return;
        }
        // Only proceed if the tapped square is empty
        if (gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;
            img.setTranslationY(-1000f); // Start animation from above the screen

            switch (activePlayer) {
                case 0:
                    img.setImageResource(R.drawable.x);
                    activePlayer = 1;
                    // Swap indicator colors: User A (X) becomes red, User B (O) becomes green
                    updateStatus(R.string.user_b_turn,
                            ContextCompat.getColor(this, R.color.inactive_red),
                            ContextCompat.getColor(this, R.color.active_green));
                    break;
                case 1:
                    img.setImageResource(R.drawable.o);
                    activePlayer = 0;
                    // Swap indicator colors: User A (X) becomes green, User B (O) becomes red
                    updateStatus(R.string.user_a_turn,
                            ContextCompat.getColor(this, R.color.active_green),
                            ContextCompat.getColor(this, R.color.inactive_red));
                    break;
            }

            // Animate the X or O falling into place
            img.animate().translationYBy(1000f).setDuration(300);
            checkWinner();
        }
    }

    /**
     * Updates the turn status text and the scoreboard colors.
     */
    private void updateStatus(int statusResId, int colorA, int colorB) {
        ((TextView) findViewById(R.id.status)).setText(statusResId);
        ((TextView) findViewById(R.id.userA)).setTextColor(colorA);
        ((TextView) findViewById(R.id.userB)).setTextColor(colorB);
    }

    /**
     * Checks if the current move resulted in a win or a draw.
     */
    private void checkWinner() {
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {

                // We have a winner!
                gameActive = false;
                int winnerMsg = (gameState[winPosition[0]] == 0) ? R.string.x_won : R.string.o_won;
                Toast.makeText(this, getString(winnerMsg), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check if the board is full (Draw)
        if (isDraw()) {
            gameActive = false;
            Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns true if all squares are filled and no one has won.
     */
    private boolean isDraw() {
        for (int state : gameState) {
            if (state == 2) return false;
        }
        return true;
    }

    /**
     * Resets the game board and all states for a new match.
     */
    private void gameReset() {
        gameActive = true;
        activePlayer = 0;
        Arrays.fill(gameState, 2);
        updateStatus(R.string.user_a_turn, 
                ContextCompat.getColor(this, R.color.active_green), 
                ContextCompat.getColor(this, R.color.inactive_red));

        // Clear all symbols from the grid UI
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int j = 0; j < gridLayout.getChildCount(); j++) {
            ((ImageView) gridLayout.getChildAt(j)).setImageResource(0);
        }
    }
}