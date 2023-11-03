package cz.cvut.fit.niccc;

import java.awt.*;

import static cz.cvut.fit.niccc.Main.FRAME_HEIGHT;

public class Vehicle {
    public static final int CAR_SIZE = 20;
    public static final int CAR_SPEED = 5;
    public static final int MARGIN = 10;

    public static final Color OUR_GREY = new Color(233,233,203);
    public static final Color OUR_RED = new Color(179,51,30);
    public static final Color OUR_YELLOW = new Color(157,104,33);
    public static final Color OUR_GREEN = new Color(37,120,112);
    private int x;
    private int y;
    private Color color;
    private int dx;
    private int dy;

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    boolean isFull = false;

    int blinkingTime = 0;

    public int getBlinkingTime() {
        return blinkingTime;
    }

    public void setBlinkingTime(int blinkingTime) {
        this.blinkingTime = blinkingTime;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        CAR,
        CAR_SHARING,
        BUS,
        NOT_SELECTED
    }

    Type type;

    private int numOfPassengers = 1;

    private boolean frontDirection = true;

    int lineNumber;

    public Vehicle(Color color, int lineNumber) {
        this.color = color;
        this.lineNumber = lineNumber;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void reverseMove() {
        x -= dx;
        y -= dy;
    }

    public void changeDirection() {
        if (isFrontDirection()) {
            frontDirection = false;
            this.setY(FRAME_HEIGHT/2 - 40 - (this.getLineNumber() - 1) * (MARGIN + CAR_SIZE + MARGIN) + MARGIN);
        } else {
            frontDirection = true;
            this.setY(FRAME_HEIGHT/2 + (this.getLineNumber() - 1) * (MARGIN + CAR_SIZE + MARGIN) + MARGIN);
        }
    }

    public void calculatePosition() {
        this.setX(20);
        this.setY(FRAME_HEIGHT / 2 + MARGIN + MARGIN * (this.getLineNumber() - 1) + (this.getLineNumber() - 1) * 30);
    }

    public void increaseLineNumber() {
        int temp = this.getLineNumber();
        temp++;
        this.setLineNumber(temp);
    }

    public void decreaseLineNumber() {
        int temp = this.getLineNumber();
        temp--;
        this.setLineNumber(temp);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFrontDirection() {
        return frontDirection;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void addPassenger() {
        numOfPassengers++;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public void setNumOfPassengers(int numOfPassengers) {
        this.numOfPassengers = numOfPassengers;
    }
}
