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

  getSize() {
    const { boxWidth, boxHeight } = this.state;
    return boxWidth * boxHeight;
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
    this.setState(
      produce((draft) => {
        solution.forEach(({ row, column, digit }) => {
          const key = `${row}-${column}`;
          if (draft.puzzle.has(key)) {
            return;
          }

          draft.puzzle.set(key, { row, column, digit });
        });
      })
    );
  }

  onClear() {
    this.setState(produce((draft) => draft.puzzle.clear()));
  }

  async onSubmit() {
    const { boxHeight, boxWidth, puzzle } = this.state;
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
        domain: this.getDomain(),
        givens,
      }),
    });
    const json = await response.json();

    this.onSolve(json);
  }

  getDomain() {
    const { puzzle } = this.state;
    const puzzleSize = this.getSize();

    const domain = Array.from(puzzle.values()).reduce((acc, { digit }) => {
      acc.add(digit);
      return acc;
    }, new Set());

    if (domain.size > puzzleSize) {
      throw new Error("Domain is too large for puzzle.");
    }

    for (let i = 49; domain.size < puzzleSize; ++i) {
      domain.add(String.fromCharCode(i));
    }
    return Array.from(domain);
  }

  render() {
    const { boxHeight, boxWidth } = this.state;

    return (
      <>
        <div id="size-label">size</div>
        <div id="adjust-width">
          <label htmlFor="width">
            <CgArrowsHAlt />
          </label>
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
          <label htmlFor="height">
            <CgArrowsVAlt />
          </label>
          <input
            id="height"
            value={boxHeight}
            type="number"
            min="2"
            className="size-input"
            onChange={(e) => this.setState({ boxHeight: e.target.value })}
          />
        </div>

        <button name="clear" onClick={this.onClear}>
          clear
        </button>

        <button name="solve" onClick={this.onSubmit}>
          solve
        </button>

        <Sudoku onUpdate={this.onUpdateGrid} {...this.state} />
      </>
    );
  }
}
