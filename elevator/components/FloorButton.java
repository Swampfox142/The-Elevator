package elevator.components;

public class FloorButton extends Button {

    private final int floor; // id of floor it points to

    public int getFloor() {
        return this.floor;
    }

    public FloorButton(int floor) {
        super();
        this.floor = floor;
    }
}
