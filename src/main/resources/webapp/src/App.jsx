import _ from "lodash";
import React from "react";
import produce, { enableMapSet } from "immer";
import { parse } from "query-string";

enableMapSet();

import Sudoku from "./components/Sudoku";

export default class App extends React.Component {
  constructor(props) {
    super(props);

    const { boxHeight, boxWidth, puzzle } = parse(location.search);

    this.state = {
      boxheight: boxHeight || 3,
      boxwidth: boxWidth || 3,
      puzzle: App.readPuzzle(boxHeight * boxWidth, puzzle) || new Map(),
    };

    _.bindAll(this, "onUpdateField", "onUpdateGrid", "onClear", "onSubmit");
  }

  static readPuzzle(size, puzzle) {
    if (!puzzle) {
      return null;
    }

    let row = 1;
    let column = 1;
    const map = new Map();

    puzzle.split('').forEach(char => {
      if (char != '-') {
        map.set(`${row}-${column}`, { row, column, digit: char, isGiven: true })
      }
      column = column == size ? 1 : column + 1;
      row = column == 1 ? row + 1 : row;
    });
    return map;
  }

  onUpdateField(evt) {
    const { target } = evt;

    this.setState({
      [target.name]: [target.value],
    });
  }

  onUpdateGrid(row, column, digit = "") {
    this.setState(
      produce((draft) => {
        if (digit.length) {
          draft.puzzle.set(`${row}-${column}`, {
            row,
            column,
            digit,
            isGiven: true,
          });
        } else {
          draft.puzzle.delete(`${row}-${column}`);
        }
      })
    );
  }

  onClear() {
    this.setState(produce((draft) => draft.puzzle.clear()));
  }

  async onSubmit() {
    const { boxheight, boxwidth, puzzle } = this.state;
    const givens = Array.from(puzzle.values()).map(({ row, column, digit }) =>
      Object.assign({ row, column, digit })
    );

    let response = await window.fetch("http://localhost:7000/solve", {
      method: "post",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        boxHeight: boxheight,
        boxWidth: boxwidth,
        domain: ['1', '2', '3', '4', '5', '6', '7', '8', '9'],
        givens,
      }),
    });
    console.log(response);  
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

        <button name="solve" onClick={this.onSubmit}>
          solve
        </button>

        <Sudoku onUpdate={this.onUpdateGrid} {...this.state} />
      </>
    );
  }
}
