import React, { useReducer } from "react";
import _ from "lodash";
import { css, cx } from "@emotion/css";

import Cell from "./Cell";

import "./Sudoku.css";

const Sudoku = (props) => {
  const { boxheight, boxwidth, puzzle, onUpdate } = props;
  const size = boxheight * boxwidth;

  const gridTemplateColumns = css`
    grid-template-columns: repeat(${size}, var(--cell-width));
  `;

  return (
    <div class={cx("grid", "sudoku", gridTemplateColumns)}>
      {_.range(1, size + 1).map((row) =>
        _.range(1, size + 1).map((column) => (
          <Cell
            updateCell={_.partial(onUpdate, row, column)}
            value={puzzle.get(`${row}-${column}`)?.digit}
            classNames={cx({
              borderTop: row % boxheight == 1,
              borderBottom: row % boxheight == 0,
              borderLeft: column % boxwidth == 1,
              borderRight: column % boxwidth == 0,
            })}
          />
        ))
      )}
    </div>
  );
};

export default Sudoku;
