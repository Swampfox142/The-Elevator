package elevator.components;

public class CallButton extends Button {

    private final Direction direction;
    private final String name;

    public Direction getDirection() {
        return this.direction;
    }

    public String getName() {
        return this.name;
    }

    public CallButton(Direction direction) {
        super();
        this.direction = direction;
        switch (direction) {
            case UP:
                this.name = "Up";
                break;
            case DOWN:
                this.name = "Down";
                break;
            default:
                this.name = "Unnamed button";
                break;
        }
    }
}
