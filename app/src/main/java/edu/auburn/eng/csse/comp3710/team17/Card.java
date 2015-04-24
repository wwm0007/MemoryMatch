package edu.auburn.eng.csse.comp3710.team17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Button;
import android.graphics.drawable.Drawable;
import java.util.List;
import android.widget.ImageButton;
import android.view.View;

public class Card {

    //button of card displays image
    public ImageButton button;
    private int index;
    //whether card has been matched in game
    private boolean match;
    //id used to determine if cards are identical
    private int id;
    //back image received when creating card
    private Drawable back;


    //creating a card takes parameters of the card's button, its image, and the drawableId which will be the card's Id as well.
    public Card(ImageButton button, int drawId, Drawable backImage) {

        match = false;
        this.id = drawId;

        back = backImage;
        this.button = button;
        this.button.setVisibility(View.VISIBLE);

        this.faceDown();

    }
    /**
     * getter of button
     * returns card's button
     * */
    public ImageButton getButton() {
        return this.button;
    }
    /**
     * turns card face down
     * if a match, make invisible, else change image to back
     * */
    public void faceDown() {
        if (!this.match) {
            this.button.setBackgroundResource(R.drawable.au);
        } else {
            this.button.setBackgroundResource(R.drawable.au);
            this.button.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * determines if two cards are identical
     * returns true or false
     * */
    public boolean equals(Card card2) {
        return this.id == card2.getId();
    }

    /**
     * turn card over to reveal the image
     * 
     * */
    public void turnOver() {
        
        if (android.os.Build.VERSION.SDK_INT >= 16)
            this.button.setBackground(back);
        else
            this.button.setBackgroundDrawable(back);

    }

    /**
     * getter of card id
     * returns card's id
     * */
    public int getId() {
        return id;
    }
    /**
     * setter of boolean status matched
     * 
     * */
    public void setMatch() {
        this.match = true;
    }

}
