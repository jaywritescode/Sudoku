import _ from "lodash";
import React, { useState } from "react";

import Sudoku from "./components/Sudoku";

export default class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      boxheight: 3,
      boxwidth: 3,
      puzzle: {},
    };

    _.bindAll(this, "onUpdateField", "onUpdateGrid");
  }

  onUpdateField(evt) {
    const { target } = evt;

    this.setState({
      [target.name]: [target.value],
    });
  }

  onUpdateGrid(row, column, digit = "") {
    this.setState({
      puzzle: {
        ...this.state.puzzle,
        [row]: {
          ...this.state.puzzle[row],
          [column]: digit,
        },
      },
    });
  }

  renderDimensionBox(dimension) {
    const name = "box" + dimension;
    const value = this.state[name];

    return (
      <>
        <label for={name}>Box {dimension}</label>
        <input
          name={name}
          value={value}
          type="number"
          min="2"
          onChange={this.onUpdateField}
        />
      </>
    );
  }

  render() {
    return (
      <>
        {this.renderDimensionBox("height")}
        {this.renderDimensionBox("width")}

        {/* <button name="reset" onClick={() => this.setState({ puzzle: {} })}>reset</button> */}

        <Sudoku onUpdate={this.onUpdateGrid} {...this.state} />
      </>
    );
  }
}
