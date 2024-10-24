
import elevator.Elevator;
import elevator.components.Direction;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;

public class ElevatorDisplay extends JPanel {

    private final Elevator elevator;
    private final int windowWidth;
    private final int windowHeight;
    private final int xBorder;
    private final int yBorder;
    private final int floorHeight;

    public ElevatorDisplay(Elevator elevator, int width, int height) {
        this.elevator = elevator;
        this.windowWidth = width;
        this.windowHeight = height;
        this.xBorder = this.windowWidth / 100;
        this.yBorder = this.windowHeight / 100;
        this.floorHeight = (this.windowHeight - (yBorder * 2)) / (this.elevator.getFloors().length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        // Calculate  Variables to be used

        int elevatorWidth = this.windowWidth / 10;
        int elevatorHeight = this.windowHeight / 10;
        int elevatorLeftX = (this.windowWidth / 2) - (elevatorWidth / 2);
        int elevatorTopY = (this.windowHeight - yBorder) - (int) (floorHeight * (this.elevator.getPosition())) - elevatorHeight;
        this.drawElevator(g, elevatorWidth, elevatorHeight, elevatorLeftX, elevatorTopY);
        this.drawFloors(g, elevatorLeftX, elevatorWidth);

        // Draw Up/Down indicators
        int arrowYTop = yBorder;
        int arrowYBottom = yBorder * 8;
        Color upArrowColor = Color.BLACK;
        Color downArrowColor = Color.BLACK;
        if (this.elevator.getDirection() == Direction.UP) {
            upArrowColor = Color.YELLOW;
        } else if (this.elevator.getDirection() == Direction.DOWN) {
            downArrowColor = Color.YELLOW;
        }
        this.drawArrow(g, elevatorLeftX, arrowYBottom, elevatorLeftX, arrowYTop, upArrowColor);
        this.drawArrow(g, elevatorLeftX + elevatorWidth, arrowYTop, elevatorLeftX + elevatorWidth, arrowYBottom, downArrowColor);

    }

    private void drawElevator(Graphics g, int elevatorWidth, int elevatorHeight, int elevatorLeftX, int elevatorTopY) {
        int totalButtons = this.elevator.getFloorButtons().length + this.elevator.getFloorButtons().length;
        int buttonRadius = (int) Math.sqrt((0.6 * elevatorHeight * elevatorWidth) / (Math.PI * totalButtons));
        int buttonDiameter = 2 * buttonRadius;
        int buttonGridDimensions = (int) Math.sqrt(totalButtons);
        int buttonGridSpacing = (elevatorWidth / buttonGridDimensions);
        // Draw Elevator
        g.setColor(Color.GRAY);
        g.drawRect(elevatorLeftX, elevatorTopY, elevatorWidth, elevatorHeight);
        // Draw Elevator Buttons
        int buttonCounter = 0;
        for (int x = 0; x < buttonGridDimensions; x++) {
            int xCoord = elevatorLeftX + (buttonGridSpacing * (x + 1)) - (buttonGridSpacing / 2) - buttonRadius;
            for (int y = 0; y < buttonGridDimensions; y++) {
                int yCoord = elevatorTopY + (buttonGridSpacing * (y + 1)) - (buttonGridSpacing / 2) - buttonRadius;
                if (buttonCounter < this.elevator.getFloorButtons().length) {
                    if (this.elevator.getFloorButton(buttonCounter).pressed()) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(xCoord, yCoord, buttonDiameter, buttonDiameter);
                    } else {
                        g.setColor(Color.BLACK);
                        g.drawOval(xCoord, yCoord, buttonDiameter, buttonDiameter);
                    }
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(this.elevator.getFloorButton(buttonCounter).getFloor()), xCoord + (buttonDiameter / 2), yCoord + (buttonDiameter / 2));
                }
                // TODO: Finish debugging & implementing non-floor elevator buttons display (door open/close, emergency stop, etc.)
                // else if (elevator.floorButtons.length - buttonCounter < elevator.elevatorButtons.length) {
                //     if (elevator.elevatorButtons[elevator.floorButtons.length - buttonCounter].pressed) {
                //         g.setColor(Color.YELLOW);
                //         g.fillOval(xCoord, yCoord, buttonDiameter, buttonDiameter);
                //     } else {
                //         g.setColor(Color.BLACK);
                //         g.drawOval(xCoord, yCoord, buttonDiameter, buttonDiameter);
                //     }
                //     g.setColor(Color.BLACK);
                //     g.drawString(String.valueOf(elevator.elevatorButtons[elevator.floorButtons.length - buttonCounter].name), xCoord + (buttonDiameter / 2), yCoord + (buttonDiameter / 2));
                // }
                buttonCounter++;
            }

        }
    }

    private void drawFloors(Graphics g, int elevatorLeftX, int elevatorWidth) {
        for (int i = 0; i < this.elevator.getFloors().length; i++) {
            int floorY = (this.windowHeight - yBorder) - (floorHeight * i);
            // Draw Floor Lines and Labels
            g.setColor(Color.BLACK);
            g.drawLine(xBorder, floorY, elevatorLeftX, floorY);
            g.drawLine(elevatorLeftX + elevatorWidth, floorY, this.windowWidth - xBorder, floorY);
            g.drawString(elevator.getFloor(i).getName(), elevatorLeftX + (elevatorWidth * 2), (int) (floorY - yBorder));
            // Draw Call Buttons
            for (int j = 0; j < this.elevator.getFloor(i).getCallButtons().length; j++) {
                int diameter = floorHeight - (yBorder * 8);
                int xCoord = ((xBorder * 4) * j) + (j * diameter);
                int yCoord = floorY - diameter - (yBorder * 4);
                if (this.elevator.getFloor(i).getCallButton(j).pressed()) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(xCoord, yCoord, diameter, diameter);
                } else {
                    g.setColor(Color.BLACK);
                    g.drawOval(xCoord, yCoord, diameter, diameter);
                }

                g.setColor(Color.BLACK);
                g.drawString(this.elevator.getFloor(i).getCallButton(j).getName(), xCoord + (diameter / 2), yCoord + (diameter / 2));
            }

        }
    }

    private void drawArrow(Graphics g, int startX, int startY, int endX, int endY, Color arrowColor) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw the main line of the arrow
        g2d.setStroke(new BasicStroke(2)); // Set the stroke width
        g2d.setColor(arrowColor); // Set the color for the arrow
        g2d.draw(new Line2D.Double(startX, startY, endX, endY)); // Draw the line

        // Calculate the arrowhead
        int arrowSize = 10;
        double angle = Math.atan2(endY - startY, endX - startX);

        // Arrowhead points
        int x1 = endX - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = endY - (int) (arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = endX - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = endY - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        // Create a polygon for the arrowhead
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(endX, endY);
        arrowHead.addPoint(x1, y1);
        arrowHead.addPoint(x2, y2);

        // Fill the arrowhead with the specified color
        g2d.fill(arrowHead);
        g2d.setColor(arrowColor);
        g2d.draw(arrowHead);
    }

    public void stop() {

        System.exit(0);
    }
}
