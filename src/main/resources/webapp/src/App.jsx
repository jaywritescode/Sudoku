import _ from "lodash";
import React, { useState } from "react";
import produce, { enableMapSet } from "immer";

enableMapSet();

import Sudoku from "./components/Sudoku";

export default class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      boxheight: 3,
      boxwidth: 3,
      puzzle: new Map(),
    };

    _.bindAll(this, "onUpdateField", "onUpdateGrid", "onClear");
  }

  onUpdateField(evt) {
    const { target } = evt;

    this.setState({
      [target.name]: [target.value],
    });
  }

  onUpdateGrid(row, column, digit = "") {
    this.setState(produce(draft => {
      if (digit.length) {
        draft.puzzle.set(`${row}-${column}`, { row, column, digit, isGiven: true });
      }
      else {
        draft.puzzle.delete(`${row}-${column}`);
      }
    }));
  }

  onClear() {
    this.setState({
      puzzle: {},
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

        <button name="reset" onClick={this.onClear}>
          reset
        </button>

        <Sudoku onUpdate={this.onUpdateGrid} {...this.state} />
      </>
    );
  }
}
