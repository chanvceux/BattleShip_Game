package BattleShip;

import java.io.Serializable;

public class Box implements Serializable {
    private int x;
    private int y;
    private Picture picture;
    private boolean isOpen = false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Box(Picture picture, int x, int y) {
        this.picture = picture;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

