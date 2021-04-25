import React from "react";
import _ from "lodash";
import { css, cx } from "@emotion/css";

import Cell from "./Cell";

import "./Sudoku.css";

const Sudoku = (props) => {
  const { boxHeight, boxWidth, puzzle, onUpdate } = props;
  const size = boxHeight * boxWidth;

  const gridTemplateColumns = css`
    grid-template-columns: repeat(${size}, var(--cell-width));
  `;

  return (
    <main role="main" className={cx("grid", "sudoku", gridTemplateColumns)}>
      {_.range(1, size + 1).map((row) =>
        _.range(1, size + 1).map((column) => (
          <Cell
            key={`${row}-${column}`}
            updateCell={_.partial(onUpdate, row, column)}
            value={puzzle.get(`${row}-${column}`)?.digit}
            classNames={cx({
              borderTop: row % boxHeight == 1,
              borderBottom: row % boxHeight == 0,
              borderLeft: column % boxWidth == 1,
              borderRight: column % boxWidth == 0,
            })}
          />
        ))
      )}
    </main>
  );
};

export default Sudoku;
