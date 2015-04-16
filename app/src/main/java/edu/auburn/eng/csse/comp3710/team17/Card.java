package edu.auburn.eng.csse.comp3710.team17;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Button;
import android.graphics.drawable.Drawable;
import java.util.List;
import android.widget.ImageButton;
import android.view.View;

public class Card {


    public ImageButton button;
    private int index;
    private boolean match;
    private int id;
    private Drawable back;



    public Card(ImageButton button, Drawable cb, int index) {

        match = false;
        this.id = index;

        back = cb;
        this.button = button;
        this.button.setVisibility(View.VISIBLE);

        this.faceDown();

    }

    public ImageButton getButton() {
        return this.button;
    }

    public void faceDown() {
        if (!this.match) {
            this.button.setBackgroundResource(R.drawable.ic_launcher);
        } else {
            this.button.setBackgroundResource(R.drawable.ic_launcher);
            this.button.setVisibility(View.INVISIBLE);
        }
    }

    public boolean equals(Card card2) {
        return this.id == card2.getId();
    }

    public void turnOver() {
        //used setBackgroundDrawable android says thats deprecated,
        // vs setBackground .
        if (android.os.Build.VERSION.SDK_INT >= 16)
            this.button.setBackground(back);
        else
            this.button.setBackgroundDrawable(back);

    }


    public int getId() {
        return id;
    }

    public void setMatch() {
        this.match = true;
    }

}