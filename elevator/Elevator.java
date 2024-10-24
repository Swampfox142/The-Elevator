package elevator;

import elevator.components.CallButton;
import elevator.components.Direction;
import elevator.components.ElevatorButton;
import elevator.components.Floor;
import elevator.components.FloorButton;

public class Elevator {

    private final Object lock; // synchronization lock to prevent data corruption when elevator data is being simultaneously written and read
    private Floor[] floors;  // ordered array of accessible floors that the elevator spans
    private FloorButton[] floorButtons; // buttons corresponding to a specific floor
    private ElevatorButton[] elevatorButtons; // all miscellaneous buttons in the elvator that do not point to a floor
    private int currentFloor; // index of floor array that elevator is currently on
    private double position; //more accurate position for UI
    private Direction direction; // direction in which the elevator is headed; null for awaiting input
    public static final int TRAVEL_TIME = 3000; // time in ms taken to reach another floor
    public static final int DOOR_TIME = 3000; // time in ms taken to open and close elevator doors

    // Getters
    public Floor[] getFloors() {
        return this.floors;
    }

    public Floor getFloor(int floorNumber) {

        return this.floors[floorNumber];

    }

    public FloorButton[] getFloorButtons() {
        return this.floorButtons;
    }

    public FloorButton getFloorButton(int floorNumber) {

        return this.floorButtons[floorNumber];

    }

    public ElevatorButton[] getElevatorButtons() {
        return this.elevatorButtons;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public double getPosition() {
        return this.position;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Elevator(Object lock, Floor[] floors, FloorButton[] floorButtons, ElevatorButton[] elevatorButtons) {
        this.lock = lock;
        this.floors = floors;
        this.floorButtons = floorButtons;
        this.elevatorButtons = elevatorButtons;
        this.currentFloor = 0;
        this.position = 0;
        this.direction = null;

    }

    public Elevator(Object lock, Floor[] floors, FloorButton[] floorButtons, ElevatorButton[] elevatorButtons, int currentFloor) {
        this(lock, floors, floorButtons, elevatorButtons);
        this.currentFloor = currentFloor;
        this.position = currentFloor;
    }

    // Constructor pre-loaded with elevator data for easy testing of the program
    public Elevator(Object lock) {
        // Initialize Objects
        int numfloors = 5;
        Floor[] elevatorFloors = new Floor[numfloors];
        FloorButton[] buttons = new FloorButton[numfloors];
        for (int i = 0; i < numfloors; i++) {
            // ground floor - only an up button
            if (i == 0) {
                CallButton upButton = new CallButton(Direction.UP);
                CallButton[] callButtons = {upButton};
                Floor groundFloor = new Floor("Ground Floor", callButtons);
                elevatorFloors[i] = groundFloor;
                buttons[i] = new FloorButton(i);
            } // top floor - only a down button
            else if (i == numfloors - 1) {
                CallButton downButton = new CallButton(Direction.DOWN);
                CallButton[] callButtons = {downButton};
                Floor topFloor = new Floor("Top Floor", callButtons);
                elevatorFloors[i] = topFloor;
                buttons[i] = new FloorButton(i);
            } // other floors
            else {
                CallButton upButton = new CallButton(Direction.UP);
                CallButton downButton = new CallButton(Direction.DOWN);
                CallButton[] callButtons = {upButton, downButton};
                Floor floor = new Floor("Floor " + i, callButtons);
                elevatorFloors[i] = floor;
                buttons[i] = new FloorButton(i);
            }
        }
        // Initialize elevator
        this.lock = lock;
        this.floors = elevatorFloors;
        this.floorButtons = buttons;
        this.elevatorButtons = new ElevatorButton[5]; // placeholder for now, as no elevator buttons beyond floor buttons have been implemented yet
        this.currentFloor = 0;
        this.position = 0;
        this.direction = null;
    }

    /**
     * Method that simulates the elevator moving upward one floor and
     * opening/closing doors. Performs checks to determine button resolutions
     * and next direction to move.
     */
    private void moveUp() {
        // simulate elevator taking time to move up one floor
        try {

            for (int i = 0; i <= TRAVEL_TIME; i++) {
                Thread.sleep(1);
                synchronized (this.lock) {
                    this.position += (double) 1 / TRAVEL_TIME;

                }
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }
        boolean makeStop = false;
        synchronized (this.lock) {

            this.currentFloor++;

        }
        // upon reaching a new floor, perform logical calculations to determine which buttons to resolve (if any), 
        // to stop or not, and which direction to go in next
        Direction nextDirection = this.determineNewDirection();
        synchronized (this.lock) {
            if (this.floorButtons[this.currentFloor].pressed()) {
                makeStop = true;
                this.floorButtons[this.currentFloor].resolve();
            }

            for (CallButton callButton : this.floors[this.currentFloor].getCallButtons()) {
                if ((callButton.getDirection() == Direction.UP && callButton.pressed()) || (callButton.getDirection() != Direction.UP && callButton.pressed() && nextDirection != Direction.UP)) {
                    makeStop = true;
                    callButton.resolve();
                }
            }
        }

        // TODO: Implement animation handling for opening/closing doors similar to the elevator movement animation above
        // TODO: Implement state for making a stop, with functional open/close door buttons
        if (makeStop) {
            // simulate elevator taking time to open and close doors at the stop (sleep placeholder for now)
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }

        }
        this.direction = nextDirection;
    }

    /**
     * Method that simulates the elevator moving downward one floor and
     * opening/closing doors. Performs checks to determine button resolutions
     * and next direction to move.
     */
    private void moveDown() {
        // simulate elevator taking time to move down one floors
        try {

            for (int i = 0; i <= TRAVEL_TIME; i++) {
                Thread.sleep(1);
                synchronized (this.lock) {
                    this.position -= (double) 1 / TRAVEL_TIME;
                }
            }
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }
        boolean makeStop = false;
        synchronized (this.lock) {

            this.currentFloor--;

        }
        // upon reaching a new floor, perform logical calculations to determine which buttons to resolve (if any), 
        // to stop or not, and which direction to go in next
        Direction nextDirection = this.determineNewDirection();
        synchronized (this.lock) {

            if (this.floorButtons[this.currentFloor].pressed()) {
                makeStop = true;
                this.floorButtons[this.currentFloor].resolve();
            }

            for (CallButton callButton : this.floors[this.currentFloor].getCallButtons()) {
                if ((callButton.getDirection() == Direction.DOWN && callButton.pressed()) || (callButton.getDirection() != Direction.DOWN && callButton.pressed() && nextDirection != Direction.DOWN)) {
                    makeStop = true;
                    callButton.resolve();
                }
            }
        }

        // TODO: Implement animation handling for opening/closing doors similar to the elevator movement animation above
        // TODO: Implement state for making a stop, with functional open/close door buttons
        if (makeStop) {
            // simulate elevator taking time to open and close doors at the stop (sleep for now)
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }

        }
        this.direction = nextDirection;

    }

    /**
     * Method that determines the elevator's next direction based on the current
     * direction and active floorButtons and callButtons
     *
     * @param currentDirection
     * @return
     */
    private Direction determineNewDirection() {
        synchronized (this.lock) {

            if (this.direction == null) {
                if (this.checkUpperFloors()) {
                    return Direction.UP;
                } else if (this.checkLowerFloors()) {
                    return Direction.DOWN;
                } else {
                    return null;
                }
            } else {
                switch (this.direction) {
                    case UP -> {
                        // Should continue UP as long as there are any elevatorButtons or callButtons corresponding to higher floors currently pressed
                        if (this.checkUpperFloors()) {
                            return Direction.UP;
                        } // Should switch to DOWN if any other buttons are pressed
                        else if (this.checkLowerFloors()) {
                            return Direction.DOWN;
                        } // Should stop in place and await input if no buttons are pressed
                        else {
                            return null;
                        }
                    }
                    case DOWN -> {
                        // Should continue DOWN as long as there are any elevatorButtons or callButtons corresponding to lower floors currently pressed
                        if (this.checkLowerFloors()) {
                            return Direction.DOWN;

                        } // Should switch to UP if any other buttons are pressed
                        else if (this.checkUpperFloors()) {
                            return Direction.UP;
                        }// Should stop in place and await input if no buttons are pressed
                        else {
                            return null;
                        }
                    }
                    default -> {
                        if (this.checkUpperFloors()) {
                            return Direction.UP;
                        } else if (this.checkLowerFloors()) {
                            return Direction.DOWN;
                        } else {
                            return null;
                        }
                    }
                }
            }

        }
    }

    /**
     * Method that moves the elevator based on the current direction. If
     * direction is currently null, then it checks for any active floorButtons
     * or callButtons
     */
    public void move() {
        Direction readDirection;
        synchronized (this.lock) {
            readDirection = this.direction;
        }
        if (readDirection == Direction.UP) {
            this.moveUp();
        }
        if (readDirection == Direction.DOWN) {
            this.moveDown();
        } else {
            synchronized (this.lock) {
                this.direction = this.determineNewDirection();
            }
        }
    }

    /**
     * Checks if any floors above the current floor have their corresponding
     * elevatorButton pressed, or if any callButtons on those floors are pressed
     *
     * @return true if above logic is true, flase otherwise
     */
    private boolean checkUpperFloors() {
        for (int i = this.currentFloor + 1; i < this.floors.length; i++) {
            if (this.floorButtons[i].pressed()) {
                return true;
            }
            for (CallButton callButton : this.floors[i].getCallButtons()) {
                if (callButton.pressed()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if any floors below the current floor have their corresponding
     * elevatorButton pressed, or if any callButtons on those floors are pressed
     *
     * @return true if above logic is true, flase otherwise
     */
    private boolean checkLowerFloors() {
        for (int i = 0; i < this.currentFloor; i++) {
            if (this.floorButtons[i].pressed()) {
                return true;
            }
            for (CallButton callButton : this.floors[i].getCallButtons()) {
                if (callButton.pressed()) {
                    return true;
                }
            }
        }
        return false;
    }
}
