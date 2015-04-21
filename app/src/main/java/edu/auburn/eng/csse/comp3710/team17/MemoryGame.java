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
    //static variables of row and comment length
    private static int ROW = -1;
    private static int COL = -1;

    private static Object locked = new Object();

    private UpdateHandler handler;
    //public List<Integer> cardIds = new ArrayList<Integer>();
    private List<Card> cardList = new ArrayList<Card>();
    
    //array list of card images
    List<Drawable> cardBacks = new ArrayList<Drawable>();
    
    List<ImageButton> buttonList = new ArrayList<ImageButton>();

    private Context context;
    private Drawable backPic;
    private ButtonListener buttonListener;
    private TableLayout gameBoard;

    private Card selection1;
    private Card selection2;

    //game type
    private int mode;
    //attempted tries
    private int tries;
    //# of matched cards
    private int totalMatches;
    long seed = System.nanoTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        //to be used to distinguish what game mode is selected
        mode = getIntent().getIntExtra("mode", 0);
        //mode = getIntent().getStringExtra("mode", 0);     alternative method of passing intent
        
        //handler which updates card status and checks for match
        handler = new UpdateHandler();

        setContentView(R.layout.activity_memory_game);
        
        // setcontentview again?
        
        backPic = getResources().getDrawable(R.drawable.au);

        // new buttonlistener for when a card is selected
        buttonListener = new ButtonListener();

        gameBoard = (TableLayout)findViewById(R.id.TableLayout01);

        context = gameBoard.getContext();

        //Spinner selection for difficulty determines what size game board
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
                //default
            }
        });

    }
    /**
     * handler maintains game and whether card's have been correctly matched
     * 
     * */
    class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            synchronized (locked) {
                checkMatch();
            }
        }
        public void checkMatch () {
            if(selection1.equals(selection2)) {
                
                //set both cards as being matched
                selection1.setMatch();
                selection2.setMatch();
                
                //call card method to turn face down which will make invisible when matched
                selection1.faceDown();
                selection2.faceDown();
                totalMatches++;
                
                //check to see if game completed
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
                //no match, turn cards back over, set selections to null
                selection1.faceDown();
                selection2.faceDown();
            }
            selection1 = null;
            selection2 = null;
        }


    }
    /**
     * initialize settings based on spinner selection
     * 
     * */
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



    /**
     * initialize game board
     * 
     * */
    private void initialize(int c, int r, int mode) {
        ROW = r;
        COL = c;

        //remove TableRow containing spinner
        gameBoard.removeView(findViewById(R.id.TableRow01));

        //Add Table row which will contain array and columns of table layouts of views (Image Buttons)
        TableRow newRow = ((TableRow)findViewById(R.id.TableRow02));
        newRow.removeAllViews();

        gameBoard = new TableLayout(context);

        newRow.addView(gameBoard);

        //List of created cards
        cardList.clear();
        
        //no card has been selectd
        selection1 = null;
        
        //shuffle arraylist of images
        Collections.shuffle(cardBacks, new Random(seed));

        //add view of new row to tablelayout based on number of rows
        for (int i =0; i < ROW; i++) {
            gameBoard.addView(addRow(i));
        }

        selection1 = null;

        //intialize attempts
        tries = 0;
        //counter of total tries
        ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
    }
    /**
     * adds a row
     * returns TableRow
     * */
    private TableRow addRow(int x) {
        TableRow row = new TableRow(context);

        row.setHorizontalGravity(Gravity.CENTER);
        
        // create button and add to row based on column index
        for (int i = 0; i < COL; i++) {
            ImageButton button = new ImageButton(context);
            //index will match card id, for now. going to change randomization
            int index = x * COL + i;
            
            button.setId(index);
            button.setOnClickListener(buttonListener);
            //possibly not needed?
            buttonList.add(button);
            row.addView(button);
            //important, creates a card using this newly created button and passed index to be used as id
            createCard(button, index);
        }
        return row;
    }
    public void createCard(ImageButton button, int index) {
        int size = ROW * COL;
        int i;
        //because there will only be size/2 images, index for two cards will be identical
        //when determined this way, after shuffling the List of images 
        if (index >= size/2)
            i = index - size/2;
        else
            i = index;

        //create card with button, drawable, and id as parameters
        Card card1 = new Card(button, cardBacks.get(i), i);
        cardList.add(card1);
    }
    
    /**
     * Listener takes action when button pressed
     * 
     * */
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
            //first button pressed, button id matches card id, turnover card to see image
            if(selection1 == null) {
                selection1 = cardList.get(id);
                selection1.turnOver();
                
                /* eliminated for more logical method of determing attempts
                tries++;
                ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
                */
            } else {
                //second card selection turn over card and allow handler to determine match
                selection2 = cardList.get(id);
                tries++;
                selection2.turnOver();

                ((TextView)findViewById(R.id.textTries)).setText("Total Turns: "+ tries);
                TimerTask task = new TimerTask() {
                
                //ensures user has ample time to see both card images before game determines whether they match
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
