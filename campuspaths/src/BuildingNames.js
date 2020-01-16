import React, {Component} from 'react';
import Select from 'react-select'
import "./BuildingNames.css";

class BuildingNames extends Component {

    render() {
        return (
            <div id="building-names">
                <div align="center">
                BUILD PATH <br/> <br/>

                Select your start building: <br/> <br/>
                <Select id="select-1" onChange={this.props.updateStart} options={this.props.dropdown}/>
                    <br/> <br/> <br/> <br/>

                Select your end building: <br/> <br/>
                <Select id="select-2" onChange={this.props.updateEnd} options={this.props.dropdown} />
                    <br/> <br/> <br/> <br/>

                <button id="draw" onClick={this.props.get}>Draw</button>
                <button id="clear" onClick={this.props.clear}>Clear</button>
                </div>
            </div>
        );
    }
}

export default BuildingNames;
