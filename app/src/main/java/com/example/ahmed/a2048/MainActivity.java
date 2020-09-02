package com.example.ahmed.a2048;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends ActionBarActivity {
    private static final String BOARD = "board";
    private static final String SCORE = "score";
    Button B;
    float x1,x2,y1,y2,x3,y3;
    TextView Labels[][] = new TextView[4][4];
    int board[][] = new int[4][4];
    static int DOWN = 0, UP = 1, RIGHT = 2, LEFT = 3;
    TextView scoreView;
    int score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        declarations();

        intToLabels(board,Labels);
        generateRandomNumber(board);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanBoard(board);
                generateRandomNumber(board);
            }
        });
    }


    private void declarations() {

        B = (Button)findViewById(R.id.button1);
        scoreView = (TextView)findViewById(R.id.score);

        Labels[0][0] = (TextView)findViewById(R.id.a41);
        Labels[0][1] = (TextView)findViewById(R.id.a42);
        Labels[0][2] = (TextView)findViewById(R.id.a43);
        Labels[0][3] = (TextView)findViewById(R.id.a44);

        Labels[1][0] = (TextView)findViewById(R.id.a31);
        Labels[1][1] = (TextView)findViewById(R.id.a32);
        Labels[1][2] = (TextView)findViewById(R.id.a33);
        Labels[1][3] = (TextView)findViewById(R.id.a34);

        Labels[2][0] = (TextView)findViewById(R.id.a21);
        Labels[2][1] = (TextView)findViewById(R.id.a22);
        Labels[2][2] = (TextView)findViewById(R.id.a23);
        Labels[2][3] = (TextView)findViewById(R.id.a24);

        Labels[3][0] = (TextView)findViewById(R.id.a11);
        Labels[3][1] = (TextView)findViewById(R.id.a12);
        Labels[3][2] = (TextView)findViewById(R.id.a13);
        Labels[3][3] = (TextView)findViewById(R.id.a14);

        return;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP :
                x2 = event.getX();
                y2 = event.getY();
                break;
        }
        x3 = x1-x2;
        y3 = y1-y2;
        if(event.getAction() == MotionEvent.ACTION_UP) {
            boolean changed = false;
            if (x3 > 0 && Math.abs(x3) > Math.abs(y3)) {//Supposed LEFT but confused because board was printed rotated
                //Swipe Left
                for(int i = 0; i < 4; i++){
                    while(Arrange(DOWN,i,board) || Merge(DOWN,i,board)){
                        for(int j = 0; j < 4; j++){
                            Arrange(DOWN,i,board);
                            Merge(DOWN,i,board);
                        }
                        changed = true;
                    }
                    Arrange(DOWN,i,board);
                }
                B.setText("Left");
            } else if (x3 < 0 && Math.abs(x3) > Math.abs(y3)) {
                //Swipe Right
                for(int i = 0; i < 4; i++){
                    while(Arrange(UP,i,board) || Merge(UP,i,board)){
                        for(int j = 0; j < 4; j++){
                            Arrange(UP,i,board);
                            Merge(UP,i,board);
                        }
                        changed = true;
                    }
                    Arrange(UP,i,board);
                }
                B.setText("Right");
            } else if (y3 > 0 && Math.abs(x3) < Math.abs(y3)) {
                //Swipe Up
                for(int i = 0; i < 4; i++){
                    while(Arrange(RIGHT,i,board) || Merge(RIGHT,i,board)){
                        for(int j = 0; j < 4; j++){
                            Arrange(RIGHT,i,board);
                            Merge(RIGHT,i,board);
                        }
                        changed = true;
                    }
                    Arrange(RIGHT,i,board);
                }
                B.setText("Up");
            } else if (y3 < 0 && Math.abs(x3) < Math.abs(y3)) {
                //Swipe Down
                for(int i = 0; i < 4; i++){
                    while(Arrange(LEFT,i,board) || Merge(LEFT,i,board)){
                        for(int j = 0; j < 4; j++){
                            Arrange(LEFT,i,board);
                            Merge(LEFT,i,board);
                        }
                        changed = true;
                    }
                    Arrange(LEFT,i,board);
                }
                B.setText("Down");
            }
            if(changed){
                generateRandomNumber(board);
            }
            intToLabels(board, Labels);


        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putSerializable(BOARD, board);
        savedInstanceState.putInt(SCORE, score);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        savedInstanceState.getInt(SCORE);
        savedInstanceState.getSerializable(BOARD);
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void intToLabels(int board[][], TextView Labels[][]){
        scoreView.setText("Score : " + score);

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++) {
                if(board[i][j] == 0){
                    Labels[i][j].setText(" ");
                    Labels[i][j].setBackgroundColor(Color.WHITE);
                }
                else {
                    Labels[i][j].setText("" + board[i][j]);
                    if(board[i][j] == 2){
                        Labels[i][j].setBackgroundColor(Color.CYAN);
                    }else if(board[i][j] == 4){
                        Labels[i][j].setBackgroundColor(Color.BLUE);
                    }else if(board[i][j] == 8){
                        Labels[i][j].setBackgroundColor(Color.YELLOW);
                    }else if(board[i][j] == 16){
                        Labels[i][j].setBackgroundColor(Color.RED);
                    }else if(board[i][j] == 32){
                        Labels[i][j].setBackgroundColor(Color.GREEN);
                    }else if(board[i][j] == 64){
                        Labels[i][j].setBackgroundColor(Color.MAGENTA);
                    }else if(board[i][j] == 128){
                        Labels[i][j].setBackgroundColor(Color.LTGRAY);
                    }else if(board[i][j] == 256){
                        Labels[i][j].setBackgroundColor(Color.GRAY);
                    }else if(board[i][j] == 512){
                        Labels[i][j].setBackgroundColor(Color.DKGRAY);
                    }else if(board[i][j] == 1024){
                        Labels[i][j].setBackgroundColor(Color.BLACK);
                    }
                    else if(board[i][j] == 2048){
                        Labels[i][j].setBackgroundColor(Color.YELLOW);
                    }
                }

            }
        }
        return;
    }
    public void generateRandomNumber(int board[][]){
        Random rand = new Random();
        int x = rand.nextInt(4);
        int y = rand.nextInt(4);
        int f = rand.nextInt(2) + 1;

        for(int i = x; i < 4; i++){
            for(int j = y; j < 4; j++){
                if(board[i][j] == 0){
                    board[i][j] = 2*f;
                    return;
                }
            }
        }
        for(int i = 0; i <= x; i++){
            for(int j = 0; j <= y; j++){
                if(board[i][j] == 0){
                    board[i][j] = 2*f;
                    return;
                }
            }
        }
    }
    public void cleanBoard(int board[][]){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                board[i][j] = 0;
            }
        }
    }
    public boolean isOccuped(int Array[][]){
        for(int i=0; i<4; i++) // all elements are zeros
        {
            for(int j=0; j<4; j++)
            {
                if(board[i][j] == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean Arrange(int Status, int col, int a[][]){
        //deleting empty boxes inside
        boolean arranged = false;
        if(Status == DOWN){
            for(int i = 0; i < 4; i++){
                for(int j = i; j < 4; j++){
                    if(a[col][i] == 0 && a[col][j] != 0){
                        a[col][i] = a[col][j];
                        a[col][j] = 0;
                        arranged = true;
                        break;
                    }
                }
            }
        }else if(Status == UP){
            for(int i = 3; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(a[col][i] == 0 && a[col][j] != 0){
                        a[col][i] = a[col][j];
                        a[col][j] = 0;
                        arranged = true;
                        break;
                    }
                }
            }
        }    if(Status == LEFT){
            for(int i = 0; i < 4; i++){
                for(int j = i; j < 4; j++){
                    if(a[i][col] == 0 && a[j][col] != 0){
                        a[i][col] = a[j][col];
                        a[j][col] = 0;
                        arranged = true;
                        break;
                    }
                }
            }
        }else if(Status == RIGHT){
            for(int i = 3; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(a[i][col] == 0 && a[j][col] != 0){
                        a[i][col] = a[j][col];
                        a[j][col] = 0;
                        arranged = true;
                        break;
                    }
                }
            }
        }
        return arranged; //edited
    }
    public boolean Merge(int Status, int col, int board[][]){
        //if 2 next numbers are similar merge them
        boolean edited = false;

        if(Status == DOWN){
            for(int i = 0; i < 3; i++){
                if(board[col][i+1] == board[col][i] && board[col][i+1] != 0){
                    board[col][i+1] = 0;
                    board[col][i] *= 2;
                    score += board[col][i];
                    edited = true;
                }
            }
        }
        else if(Status == UP){
            for(int i = 3; i > 0; i--){
                if(board[col][i-1] == board[col][i] && board[col][i-1] != 0){
                    board[col][i-1] = 0;
                    board[col][i] *= 2;
                    score += board[col][i];
                    edited = true;
                }
            }
        }
        else if(Status == LEFT){
            for(int i = 0; i < 3; i++){
                if(board[i+1][col] == board[i][col] && board[i+1][col] != 0){
                    board[i+1][col] = 0;
                    board[i][col] *= 2;
                    score += board[i][col];
                    edited = true;
                }
            }
        }
        else if(Status == RIGHT){
            for(int i = 3; i > 0; i--){
                if(board[i-1][col] == board[i][col] && board[i-1][col] != 0){
                    board[i-1][col] = 0;
                    board[i][col] *= 2;
                    score += board[i][col];
                    edited = true;
                }
            }
        }
        return edited;
    }




}
