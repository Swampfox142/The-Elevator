package elevator.components;

public class Button {

    private boolean pressed;

    public Button() {
        this.pressed = false;
    }

    // Getter for pressed
    public boolean pressed() {
        return this.pressed;
    }

    /**
     * Function that sets a button to 'pressed'
     */
    public void press() {
        this.pressed = true;

    }

    /**
     * Function that sets a button to not pressed
     */
    public void resolve() {
        this.pressed = false;

    }
}
