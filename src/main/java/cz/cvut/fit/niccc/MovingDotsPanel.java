package cz.cvut.fit.niccc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static cz.cvut.fit.niccc.Vehicle.*;
import static cz.cvut.fit.niccc.Highway.CARS_PER_LINE;
import static cz.cvut.fit.niccc.Main.FRAME_WIDTH;

class MovingDotsPanel extends JPanel {

    Highway highway = new Highway(30, FRAME_WIDTH);
    private final ArrayList<Vehicle> vehicles;
    int loopCounter = 0;
    int loopCounter2 = 0;

    boolean ableToVote = false;     //todo refactor to numOfVotes

    int numOfVotes = 0;
    int votesForHighway = 0;
    int votesForCarSharing = 0;
    int votesForBus = 0;
    int[] highwayPartIndexes;

    public MovingDotsPanel() {

        addKeyListener(new MyKeyListener());
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();

        vehicles = new ArrayList<>();

        Timer timer = new Timer(40, e -> {
            for (Vehicle car : vehicles) {
                if (car.getX() > FRAME_WIDTH || car.getX() < -20)       //if out of screen
                    car.changeDirection();
                if (car.isFrontDirection())
                    car.move();
                else
                    car.reverseMove();
            }
            repaint(); // Redraw the canvas
        });

        timer.start();
    }


    /*void addVehicle(Vehicle vehicle) throws InterruptedException {
        for (Vehicle vehicle1 : vehicles) {
            if (vehicle1.getX() - vehicle.getX() < 25 && vehicle1.getX() - vehicle.getX() > -25) {   //todo kdyztak prohodit
                addVehicle(vehicle);
            }
        }
        vehicles.add(vehicle);
        ableToVote = true;
    }*/

    public Vehicle findVehicleByType(Type type) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.type.equals(type)) {
                return vehicle;
            }
        }
        return null;
    }

    public Vehicle findEmptyVehicleByType(Type type) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.type.equals(type) && !vehicle.isFull) {
                return vehicle;
            }
        }
        return null;
    }

    private class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == ' ' && !ableToVote) {


                MusicPlayer player = new MusicPlayer();
                player.setFileName("car_starting.WAV");
                player.setLooping(false);
                player.start();

                Vehicle vehicle = new Vehicle(OUR_GREY, 1 + vehicles.size() / CARS_PER_LINE);
                vehicle.setType(Type.NOT_SELECTED);
                vehicle.setBlinkingTime(500);   //30 sec to vote

                vehicle.calculatePosition(); //todo add car to empty space
                vehicle.setDx(CAR_SPEED);

                /*try {
                     addVehicle(vehicle);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }*/
                vehicles.add(vehicle);
                numOfVotes++;
                ableToVote = true;

            } else if (e.getKeyChar() == '1' && ableToVote) {
                votesForHighway++;
                ableToVote = false;

                Vehicle vehicle = findVehicleByType(Type.NOT_SELECTED);

                vehicle.setType(Type.CAR);
                vehicle.setColor(OUR_RED);
                vehicle.setBlinkingTime(0);
                vehicle.setFull(true);


            } else if (e.getKeyChar() == '2' && ableToVote) {
                votesForCarSharing++;
                ableToVote = false;

                Vehicle carSharingVehicle = findEmptyVehicleByType(Type.CAR_SHARING);
                Vehicle notSelectedVehicle = findVehicleByType(Type.NOT_SELECTED);


                if (carSharingVehicle != null) { //empty carSharing vehicle exits
                    carSharingVehicle.addPassenger();
                    carSharingVehicle.setBlinkingTime(100);
                    carSharingVehicle.setFull(true);
                    vehicles.remove(notSelectedVehicle);

                } else {

                    notSelectedVehicle.setType(Type.CAR_SHARING);
                    notSelectedVehicle.setColor(OUR_YELLOW);
                    notSelectedVehicle.setBlinkingTime(0);
                }

            } else if (e.getKeyChar() == '3' && ableToVote) {
                votesForBus++;
                ableToVote = false;

                Vehicle bus = findEmptyVehicleByType(Type.BUS);
                Vehicle notSelectedVehicle = findVehicleByType(Type.NOT_SELECTED);


                if (bus != null) { //empty bus vehicle exits
                    bus.addPassenger();
                    bus.setBlinkingTime(100);
                    if (bus.getNumOfPassengers() >= 30) {       //todo implement isFull() method
                        bus.setFull(true);
                    }
                    vehicles.remove(notSelectedVehicle);

                } else {
                    notSelectedVehicle.setType(Type.BUS);
                    notSelectedVehicle.setColor(OUR_GREEN);
                    notSelectedVehicle.setBlinkingTime(0);
                }
            }
        }
    }

    int localVotesForHighway;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Votes: " + numOfVotes, 10, 20);
        g.drawString("votesForHighway: " + votesForHighway, 10, 35);
        g.drawString("votesForCarSharing: " + votesForCarSharing, 10, 50);
        g.drawString("votesForBus: " + votesForBus, 10, 65);
        g.drawString("numOfCars: " + vehicles.size(), 10, 80);
        g.drawString("Able to vote: " + ableToVote, 10, 95);


        g.drawLine(0, 450, 1600, 450);

        if (localVotesForHighway < votesForHighway) {
            drawHighway(g, true);
        }
        drawHighway(g, false);

        localVotesForHighway = votesForHighway;

        //drawHighway(g);
        drawCars(g);
    }

    private void drawHighway(Graphics g, Boolean blinking) {
        g.setColor(Color.BLACK);

        if (loopCounter2 > 150)
            loopCounter2 = 0;

        loopCounter2++;

        // renders all highway lines
        for (int i = 1; i <= highway.numOfLines; i++) {
            g.fillRect(0, 455 + (i - 1) * 40, highway.getLength(), highway.getWidth());
            g.fillRect(0, 415 - (i - 1) * 40, highway.getLength(), highway.getWidth());
        }

        // if too much RED cars per line create new line
        if ((votesForHighway / CARS_PER_LINE) + 1 != highway.getNumOfLines()) {
            highway.setNumOfLines((votesForHighway / CARS_PER_LINE) + 1);
        }

        //if (blinking & loopCounter2 % 10 > 5) {
            // highway building
            g.fillRect(0, 455 + (highway.numOfLines) * 40, (votesForHighway % CARS_PER_LINE) * highway.length / CARS_PER_LINE, highway.getWidth());
            g.fillRect(highway.getLength() - (votesForHighway % CARS_PER_LINE) * highway.length / CARS_PER_LINE, 415 - (highway.numOfLines) * 40, (votesForHighway % CARS_PER_LINE) * highway.length / CARS_PER_LINE, highway.getWidth());
        //}
    }

    private void drawCars(Graphics g) {

        if (loopCounter > 150)
            loopCounter = 0;

        loopCounter++;


        ArrayList<Vehicle> vehiclesToRemove = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            if (vehicle.blinkingTime > 0 & loopCounter % 10 > 5) {
                continue;   //30 itereaci bych ho nezobrazoval
            }

            if (vehicle.blinkingTime > 0) {
                int blinkingTime = vehicle.getBlinkingTime();
                blinkingTime--;
                vehicle.setBlinkingTime(blinkingTime);
                if (blinkingTime == 0 && vehicle.type.equals(Type.NOT_SELECTED)) {
                    vehiclesToRemove.add(vehicle);
                    ableToVote = false;
                }
                //if(vehicle.) vehicle type not selected delete the car
            }

            g.setColor(vehicle.getColor());
            g.fillOval(vehicle.getX(), vehicle.getY(), CAR_SIZE, CAR_SIZE); // Draw a dot
            Toolkit.getDefaultToolkit().sync();
        }
        vehicles.removeAll(vehiclesToRemove);
    }
}
