import _ from "lodash";
import React from "react";
import produce, { enableMapSet } from "immer";
import { parse } from "query-string";
import { CgArrowsHAlt, CgArrowsVAlt } from "react-icons/cg";

enableMapSet();

import Sudoku from "./components/Sudoku";

export default class App extends React.Component {
  constructor(props) {
    super(props);

    const { boxHeight, boxWidth, puzzle } = parse(location.search);

    this.state = {
      boxHeight: boxHeight || 3,
      boxWidth: boxWidth || 3,
      puzzle: App.readPuzzle(boxHeight * boxWidth, puzzle) || new Map(),
    };

    _.bindAll(this, "onUpdateGrid", "onClear", "onSubmit");
  }

  static readPuzzle(size, puzzle) {
    if (!puzzle) {
      return null;
    }

    let row = 1;
    let column = 1;
    const map = new Map();

    puzzle.split("").forEach((char) => {
      if (char != "-") {
        map.set(`${row}-${column}`, {
          row,
          column,
          digit: char,
          isGiven: true,
        });
      }
      column = column == size ? 1 : column + 1;
      row = column == 1 ? row + 1 : row;
    });
    return map;
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

  onSolve({ solution }) {
    this.setState(produce((draft) => {
      solution.forEach(({ row, column, digit}) => {
        const key = `${row}-${column}`;
        if (draft.puzzle.has(key)) {
          return;
        }

        draft.puzzle.set(key, { row, column, digit });
      });
    }));
  }

  onClear() {
    this.setState(produce((draft) => draft.puzzle.clear()));
  }

  async onSubmit() {
    const { boxheight, boxwidth, puzzle } = this.state;
    const givens = Array.from(puzzle.values()).map(({ row, column, digit }) =>
      Object.assign({ row, column, digit })
    );

    const response = await window.fetch("/solve", {
      method: "post",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        boxHeight,
        boxWidth,
        domain: ["1", "2", "3", "4", "5", "6", "7", "8", "9"],
        givens,
      }),
    });
    const json = await response.json();

    this.onSolve(json);
  }

  render() {
    const { boxHeight, boxWidth } = this.state;

    return (
      <>
        <div id="size-label">size</div>
        <div id="adjust-width">
          <label htmlFor="width"><CgArrowsHAlt /></label>
          <input
            id="width"
            value={boxWidth}
            type="number"
            min="2"
            className="size-input"
            onChange={(e) => this.setState({ boxWidth: e.target.value })}
          />
        </div>
        <div id="adjust-height">
          <label htmlFor="height"><CgArrowsVAlt /></label>
          <input
            id="height"
            value={boxHeight}
            type="number"
            min="2"
            className="size-input"
            onChange={(e) => this.setState({ boxHeight: e.target.value })}
          />
        </div>

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
