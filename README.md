## Assumptions

## Notes on Design Decisions

Choice to use 'buttons' array to host all buttons inside an elevator, including floorButtons, emergency stop button, and open/close buttons.
Pros: - agnostic. Any type of button can be added to the array, allowing flexibility for this design to be used accross different types of elevators.
Cons: - unoptimal. Flexibility costs optimization in many cases. Some elevator logic needs to loop over a specifc group of buttons within this array. By keeping the array generic, the program must now loop over the entire array, including buttons that it need not loop over. Ideally, to optimize these code segments, there could possibly be an Elevator class design which uses more, specific button arrays, rather than a single generic one.
Decision:
Ultimately, due to lack of any requirements for this elevator design, I thought it best to keep things generic and flexible, so that many types of elevators are supported. With this direction, it is also easier to build out the current Elevator design to support elevators that are not currently supported.

Choice to use array elevator.floorButtons and elevator.floors index as the 'unique ID' for that button/floor.
Pros: - less properties in subclasses. Leaving each subclass without a specific ID field allows it to exist independently of the elevator, as it should be able to.
Cons: - less verbose code. This design is not intuitive and causes floors to not be tied to a specific hierarchy within the building (e.g. Ground->1->2->...) without being tied to an elevator first.
Decision:
Ultimately, keeping subclasses agnostic and more reusable allows some of the design to stay in-tact if, for instance, the elevator needed a redesign.

Elevator.floors and Elevator.floorButtons are ordered and both reference an elevator-accessible floor.s
