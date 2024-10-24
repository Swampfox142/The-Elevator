package elevator.components;

public class Floor {

    private final String name; // name of the floor
    private CallButton[] callButtons;

    public Floor(String name, CallButton[] callButtons) {
        this.name = name;
        this.callButtons = callButtons;
    }

    public String getName() {
        return this.name;
    }

    public CallButton[] getCallButtons() {
        return this.callButtons;
    }

    // Get call button by direction
    public CallButton getCallButton(Direction direction) {
        for (CallButton callButton : this.callButtons) {
            if (callButton.getDirection() == direction) {
                return callButton;
            }
        }
        System.out.println("Floor " + this.name + " does not have a " + direction + " button!");
        Thread.currentThread().interrupt();
        return null;

    }

    // Get call button by array index
    public CallButton getCallButton(int buttonNumber) {
        try {
            return this.callButtons[buttonNumber];
        } catch (Exception e) {
            System.out.println("Floor " + this.name + " does not have a button at array element " + buttonNumber + "!");
            Thread.currentThread().interrupt();
            throw new Error(e);
        }

    }

    public void setCallButtons(CallButton[] callButtons) {
        this.callButtons = callButtons;
    }

}
