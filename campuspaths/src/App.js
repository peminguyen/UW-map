/*
 * Copyright ©2019 Dan Grossman.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import Map from "./Map";
import BuildingNames from "./BuildingNames";
import "./App.css";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            // Store the string of the start building
            startBuilding: "",

            // Store the string of the end building
            endBuilding: "",

            // Store the list of coordinates to draw on the screen
            listCoordinates: [],

            // Store the list of building names for the two dropdown menus
            buildings_dropdown: [],

            // Store the direction
            direction: "",

            // Store the coordinate of the start building
            startCoordinate: [],

            // Store the coordinate of the end building
            endCoordinate: []
        };
    }

    async componentDidMount() {
        // Fetch the link to get the list of buildings for the two dropdown menus
        let link = "http://localhost:4567/buildings";
        let response = await fetch(link);
        let object = await response.json();

        // Store the list of building in each dropdown menu
        let buildings_dropdown = [];
        for (let line of object) {
            let line_dropdown = {label : line[1], value: line[0]};
            buildings_dropdown.push(line_dropdown)
        }
        this.setState({
            buildings_dropdown: buildings_dropdown
        })
    }

    // Get all the data from all the localhost links fetched from Spark
    getData = async () => {
        let result_coordinates = [];

        // Fetch the link to get the path from one building to another
        let link = "http://localhost:4567/findPath?start=" + this.state.startBuilding +
            "&end=" + this.state.endBuilding;

        try {
            let response = await fetch(link);
            if (!response.ok) {
                alert("The name(s) of the building(s) entered is wrong");
                return;
            }

            // Store all the start coordinates and end coordinates in the form [start_coordinate, end_coordinate]
            let object = await response.json();
            for (let i = 0; i < object.path.length; i++) {
                result_coordinates.push(
                    [[parseFloat(object.path[i].start.x),
                    parseFloat(object.path[i].start.y)],
                    [parseFloat(object.path[i].end.x),
                    parseFloat(object.path[i].end.y)]]);
            }

            this.setState({
                listCoordinates: result_coordinates,
                startCoordinate: [parseFloat(object.path[0].start.x), parseFloat(object.path[0].start.y)],
                endCoordinate: [parseFloat(object.path[object.path.length - 1].end.x), parseFloat(object.path[object.path.length - 1].end.y)]
            });

            // Print the error if the link was not fetchable
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }

        // Fetch the link to get the direction from one building to another
        let link_dir = "http://localhost:4567/getDirection?start=" + this.state.startBuilding +
            "&end=" + this.state.endBuilding;
        try {
            let response = await fetch(link_dir);
            if (!response.ok) {
                alert("The name(s) of the building(s) entered is wrong");
                return;
            }

            // Store the direction
            let object_dir = await response.json();
            this.setState({
                direction: object_dir
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    // Reset all the coordinates and directions to default to clear the path and objects drawn on the map
    clearData = () => {
        this.setState({
            listCoordinates: [],
            direction: "",
            startCoordinate: [],
            endCoordinate: []
        });
    };

    // Get the start building from the first dropdown menu
    updateStart = (input) => {
        this.setState({startBuilding: input.value});

    };

    // Get the end building from the second dropdown menu
    updateEnd = (input) => {
        this.setState({endBuilding: input.value});
    };

    render() {
        // Store the direction to print to the screen
        let result = [];
        for (let line of this.state.direction.split('\n')) {
            result.push(<div key={line}> {line}</div>);
        }

        return (
            <div>
                <div id="app-title">Find Path between two UW Buildings</div>
                <div id="directionsHolder"> DIRECTION: <br/> {result} </div>
                <Map listCoordinates={this.state.listCoordinates}
                     startCoordinate={this.state.startCoordinate}
                     endCoordinate={this.state.endCoordinate}
                />
                <BuildingNames  updateStart={this.updateStart}
                                updateEnd={this.updateEnd}
                                clear={this.clearData} get={this.getData}
                                dropdown={this.state.buildings_dropdown}
                />
            </div>
        );
    }
}
export default App;
