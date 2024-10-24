# Elevator Control System

## Table of Contents
1. [Description](#description)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Features](#features)
5. [Bugs](#bugs)


## Description
**The Elevator** is a simulation of an elevator system written in Java. It provides the user with cli control of a configurable elevator system that will output the animated system with Swing.

- Handles real-time updates of elevator positions.
- Performs optimization calculations to simulate actual elevator behavior.
- Allows for interaction via command-line.
- Scalable & configurable with arbitrary number of floors/buttons (not currently supported through cli, and not thoroughly tested)

## Installation
1. Clone the repository:
   git clone https://github.com/Swampfox142/The-Elevator.git
2. Navigate to the project directory:
   cd The-Elevator
3. Compile the Java source code:
   javac -d build RunElevator.java
4. Run the application:
   java -cp build RunElevator

## Usage
1. Enter commands to control the elevator, such as:
   - '3': presses the '3' button while inside the elevator.
   - '3 up': presses the 'up' button on floor 3, outside of the elevator.
2. View the Swing output that will animate the button presses and the elevator moving from floor to floor.
3. Enter 'exit' to stop the application.

## Features - Implemented and Planned
- [x] Real-time position tracking and elevator animation
- [x] Real-time button highlights and updates
- [x] Controllable buttons
- [x] Scalable, agnostic design
- [ ] Open/Close door buttons (planned)
- [ ] Emergency Stop button (planned)
- [ ] GUI-supported user input (unplanned)
- [ ] Multiple Elevators (unplanned)



## Bugs
- UI does not scale dynamically when window size is changed while the application is running.
- Bottom part of UI is cut off the bottom part of the window (full-screen pr dragging the window down fixes the visibility).
