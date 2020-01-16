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
import "./Map.css";

class Map extends Component {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    constructor(props) {
        super(props);
        this.state = {
            // Store the background image
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
        this.drawLines();
        this.drawCircles();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }


    drawBackgroundImage() {
        let canvas = this.canvas.current;
        let ctx = canvas.getContext("2d");

        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
    }

    // Draw the two circles for the start building and the end building
    drawCircles = () => {
        // Draw the circles only when there's a path
        if (this.props.startCoordinate.length > 0 || this.props.startCoordinate.length > 0){
            let canvas = this.canvas.current;
            let ctx = canvas.getContext("2d");
            this.drawOneCircle(ctx, this.props.startCoordinate, "yellow", 15);
            this.drawOneCircle(ctx, this.props.endCoordinate, "blue", 30);
        }
    };

    drawOneCircle = (ctx, c, color, radius) => {
        ctx.fillStyle = color;
        ctx.beginPath();
        ctx.arc(c[0], c[1], radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    // Draw all the lines connecting different segments of the path from one building to another building
    drawLines = () => {
        // If the list of coordinates is not empty, draw the path
        if (this.props.listCoordinates.length > 0) {
            let canvas = this.canvas.current;
            let ctx = canvas.getContext("2d");
            for (let coordinate of this.props.listCoordinates){
                this.drawOneLine(ctx, coordinate[0], coordinate[1]);
            }
        }
    };

    // Draw one line between two coordinates
    drawOneLine = (ctx, c1, c2) => {
        ctx.beginPath();
        ctx.strokeStyle = "red";
        ctx.lineWidth = "10";
        ctx.moveTo(c1[0], c1[1]);
        ctx.lineTo(c2[0], c2[1]);
        ctx.stroke();
    };


    render() {
        return (
            <div>
            <canvas ref={this.canvas}/>
            </div>
        )
    }
}
export default Map;