import React, { useReducer } from "react";
import _ from "lodash";
import { css, cx } from "@emotion/css";

import Cell from "./Cell";

import "./Sudoku.css";

const Sudoku = (props) => {
  const { boxHeight, boxWidth } = props;
  const size = boxHeight * boxWidth;

  const reducer = (state, action) => {
    const { row, column, digit } = action.payload;

    switch (action.type) {
      case "set": {
        return { ...state, [row]: { ...state[row], [column]: digit } };
      }
      case "unset": {
        const copy = _.cloneDeep(state);

        delete copy[row][column];

        return copy;
      }
      case "clear":
        return {};
    }
  };
  const [state, dispatch] = useReducer(reducer, {});

  const gridTemplateColumns = css`
    grid-template-columns: repeat(${size}, 40px);
  `;

  const updateCell = (row, column, digit) => {
    const type = digit.length ? "set" : "unset";
    const action = {
      type,
      payload: { row, column, digit },
    };

    dispatch(action);
  };

  const clear = () => {
    dispatch({ type: "clear", payload: {} });
  };

  return (
    <div class={cx("grid", "sudoku", gridTemplateColumns)}>
      {_.range(1, size + 1).map((row) =>
        _.range(1, size + 1).map((column) => (
          <Cell
            updateCell={_.partial(updateCell, row, column)}
            classNames={cx({
              borderTop: row % boxHeight == 1,
              borderBottom: row % boxHeight == 0,
              borderLeft: column % boxWidth == 1,
              borderRight: column % boxWidth == 0,
            })}
          />
        ))
      )}
    </div>
  );
};

export default Sudoku;
