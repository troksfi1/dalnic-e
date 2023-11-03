package cz.cvut.fit.niccc;

public class Highway {

    public static final int CARS_PER_LINE = 100;

    public Highway(int width, int length) {
        this.width = width;
        this.length = length;
    }

    int width;

    public int getLength() {
        return length;
    }

    int length;

    int[] highwayPartIndexes;

    public void setNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
    }

    public int getNumOfLines() {
        return numOfLines;
    }

    int numOfLines = 1;

    void addLine() {
        numOfLines++;
    }

    public int getWidth() {
        return width;
    }
}
