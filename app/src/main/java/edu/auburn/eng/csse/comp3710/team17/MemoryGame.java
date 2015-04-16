package edu.auburn.eng.csse.comp3710.team17;

import android.app.Activity;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import android.view.Menu;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow;
import android.view.View;
import android.view.Gravity;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


public class MemoryGame extends Activity {
    private static int ROW = -1;
    private static int COL = -1;

    private static Object locked = new Object();

    private UpdateHandler handler;
    //public List<Integer> cardIds = new ArrayList<Integer>();
    private List<Card> cardList = new ArrayList<Card>();
    List<Drawable> cardBacks = new ArrayList<Drawable>();
    List<ImageButton> buttonList = new ArrayList<ImageButton>();

    private Context context;
    private Drawable backPic;
    private ButtonListener buttonListener;
    private TableLayout gameBoard;

    private Card selection1;
    private Card selection2;

    private int mode;
    private int tries;
    private int totalMatches;
    long seed = System.nanoTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        //to be used to distinguish what game mode is selected
        mode = getIntent().getIntExtra("mode", 0);
        //mode = getIntent().getStringExtra("mode", 0);
        handler = new UpdateHandler();

        setContentView(R.layout.activity_memory_game);
        // setcontentview again?
        backPic = getResources().getDrawable(R.drawable.au);

        buttonListener = new ButtonListener();

        gameBoard = (TableLayout)findViewById(R.id.TableLayout01);

        context = gameBoard.getContext();

        Spinner spinner = (Spinner) findViewById(R.id.sizeSpinner);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> a, View v, int position, long r) {
                 ((Spinner) findViewById(R.id.sizeSpinner)).setSelection(0);

                  int x, y;
                  switch(position) {
                    case 1:
                        x= 3;
                        y= 4;
                        break;
                    case 2:
                        x= 4;
                        y= 4;
                        break;
                    case 3:
                        x= 4;
                        y= 5;
                        break;
                    default:
                        return;
                }
                loadPictures(position);
                initialize( x, y, mode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> a) {

            }
        });

    }

    class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            synchronized (locked) {
                checkMatch();
            }
        }
        public void checkMatch () {
            if(selection1.equals(selection2)) {
                selection1.setMatch();
                //selection2.turnOver();
                selection2.setMatch();
                selection1.faceDown();
                selection2.faceDown();
                totalMatches++;
                if (totalMatches == ((ROW*COL)/2)) {
                    Toast.makeText(context, "Congratulations! You win!", Toast.LENGTH_SHORT).show();

                    // Reset the game after a short delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MemoryGame.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1400);
                }
            }
            else {

                selection1.faceDown();
                selection2.faceDown();
            }
            selection1 = null;
            selection2 = null;
        }


    }

    private void loadPictures(int case1) {

        switch(case1) {
            case 1:
                cardBacks.add(getResources().getDrawable(R.drawable.gus));
                cardBacks.add(getResources().getDrawable(R.drawable.nova));
                cardBacks.add(getResources().getDrawable(R.drawable.sam));
                cardBacks.add(getResources().getDrawable(R.drawable.stadium));
                cardBacks.add(getResources().getDrawable(R.drawable.aubie));
                cardBacks.add(getResources().getDrawable(R.drawable.bo));

                break;
            case 2:
                cardBacks.add(getResources().getDrawable(R.drawable.gus));
                cardBacks.add(getResources().getDrawable(R.drawable.nova));
                cardBacks.add(getResources().getDrawable(R.drawable.sam));
                cardBacks.add(getResources().getDrawable(R.drawable.stadium));
                cardBacks.add(getResources().getDrawable(R.drawable.aubie));
                cardBacks.add(getResources().getDrawable(R.drawable.bo));
                cardBacks.add(getResources().getDrawable(R.drawable.bruce));
                cardBacks.add(getResources().getDrawable(R.drawable.cam));

                break;
            case 3:
                cardBacks.add(getResources().getDrawable(R.drawable.gus));
                cardBacks.add(getResources().getDrawable(R.drawable.nova));
                cardBacks.add(getResources().getDrawable(R.drawable.sam));
                cardBacks.add(getResources().getDrawable(R.drawable.stadium));
                cardBacks.add(getResources().getDrawable(R.drawable.aubie));
                cardBacks.add(getResources().getDrawable(R.drawable.bo));
                cardBacks.add(getResources().getDrawable(R.drawable.bruce));
                cardBacks.add(getResources().getDrawable(R.drawable.cam));
                cardBacks.add(getResources().getDrawable(R.drawable.logo));
                cardBacks.add(getResources().getDrawable(R.drawable.charles));

                break;
            default:
                return;
        }

    }



    private void initialize(int c, int r, int mode) {
        ROW = r;
        COL = c;

        gameBoard.removeView(findViewById(R.id.TableRow01));

        TableRow newRow = ((TableRow)findViewById(R.id.TableRow02));
        newRow.removeAllViews();

        gameBoard = new TableLayout(context);

        newRow.addView(gameBoard);

        cardList.clear();
        //double check this
        selection1 = null;


        Collections.shuffle(cardBacks, new Random(seed));


        for (int i =0; i < ROW; i++) {
            gameBoard.addView(addRow(i));
        }

        selection1 = null;

        tries = 0;
        ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
    }
    private TableRow addRow(int x) {
        TableRow row = new TableRow(context);

        row.setHorizontalGravity(Gravity.CENTER);

        for (int i = 0; i < COL; i++) {
            ImageButton button = new ImageButton(context);
            int index = x * COL + i;
            button.setId(index);
            button.setOnClickListener(buttonListener);
            buttonList.add(button);
            row.addView(button);
            createCard(button, index);
        }
        return row;
    }
    public void createCard(ImageButton button, int index) {
        int size = ROW * COL;
        int i;
        if (index >= size/2)
            i = index - size/2;
        else
            i = index;

        Card card1 = new Card(button, cardBacks.get(i), i);
        cardList.add(card1);
    }
    class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            synchronized(locked) {
                if(selection1!=null && selection2!=null){
                    return;
                }
                int id = v.getId();
                turnFaceUp((ImageButton) v, id);



            }
        }

        private void turnFaceUp(ImageButton button, int id) {
            if(selection1 == null) {
                selection1 = cardList.get(id);
                tries++;
                selection1.turnOver();
                ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
            } else {
                selection2 = cardList.get(id);
                tries++;
                selection2.turnOver();

                ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        try {
                            synchronized (locked) {
                                handler.sendEmptyMessage(0);
                            }
                        } catch (Exception e) {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer timer = new Timer(false);
                timer.schedule(task, 1300);
            }
        }



    }
}