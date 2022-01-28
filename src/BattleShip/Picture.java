package BattleShip;

import javax.swing.*;
import java.awt.*;

public enum Picture {

    CLOSED,
    DESTROY_SHIP,
    EMPTY,
    NUM1, NUM2, NUM3, NUM4, NUM5, NUM6, NUM7, NUM8, NUM9, NUM10,
    POINT,
    SHIP,
    INFO,
    SYM1, SYM2, SYM3, SYM4, SYM5, SYM6, SYM7, SYM8, SYM9, SYM10;
    public static final int COLUMNS = 11;
    public static final int ROWS = 11;
    public static final int IMAGE_SIZE = 40;

    public static Image getImage(String nameImage) {
        String fileName = "res/img/" + nameImage.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(fileName);
        return icon.getImage();
    }
}
