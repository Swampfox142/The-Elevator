
import elevator.Elevator;
import elevator.components.Direction;
import java.awt.event.ActionEvent;
import java.util.Scanner;
import javax.swing.*;

public class RunElevator {

    private static final Object lock = new Object();
    static Elevator elevator = new Elevator(lock);
    public static final int DISPLAY_HEIGHT = 1000;
    public static final int DISPLAY_WIDTH = 1000;
    public static final int FPS = 10; // fps for animations

    public static void main(String[] args) {
        JFrame frame = new JFrame("Elevator Display");
        ElevatorDisplay panel = new ElevatorDisplay(elevator, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        frame.add(panel);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        // Create a timer to update the display based on the fps
        Timer timer = new Timer(1000 / FPS, (ActionEvent e) -> {
            panel.repaint();
        });
        timer.start();

        // Thread for accepting user input
        Thread inputThread = new Thread(() -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Enter commands to control the elevator.");
                System.out.println("Commands in the form of '{int}' will press a specified floor button within the elevator.");
                System.out.println("Commands in the form of '{int} {'up'/'down'}' will press a call button on specified floor(type 'exit' to stop).");
                System.out.println("Example: input '3' will press the floor 3 button inside the elevator");
                System.out.println("Example: input '3 up' will press the 'up' button on floor 3 outside of the elevator");
                System.out.println("Type 'exit' to quit:");
                while (true) {
                    String input = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(input)) {
                        panel.stop();
                        break;
                    }
                    String[] parts = input.split(" ");
                    int floorNumber;
                    Direction direction = null;
                    try {
                        // Check if the first part is a valid floor number
                        floorNumber = Integer.parseInt(parts[0]);

                        // If there is a second argument, check if it specifies a direction
                        if (parts.length > 1) {
                            if (parts[1].equalsIgnoreCase("up")) {
                                direction = Direction.UP;
                            } else if (parts[1].equalsIgnoreCase("down")) {
                                direction = Direction.DOWN;
                            } else {
                                System.out.println("Invalid direction. Use 'up' or 'down'.");
                                continue;
                            }
                        }

                        // Call the appropriate method based on the input
                        if (direction != null) {
                            elevator.getFloor(floorNumber).getCallButton(direction).press();
                        } else {
                            elevator.getFloorButton(floorNumber).press();
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Please provide a valid floor.");
                    }

                }
            }
        });

        // Thread for performing elevator computations
        Thread elevatorThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // Wait for .1 seconds
                    elevator.move();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        inputThread.start();
        elevatorThread.start();

        // Wait for the input thread to finish
        try {
            inputThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Stop the other threads gracefully
        elevatorThread.interrupt();
    }
}
