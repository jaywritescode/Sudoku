import React, { useState, useReducer } from "react";
import _ from "lodash";
import { css } from "@emotion/css";

import Cell from './components/Cell';

function App() {
  const [boxHeight, setBoxHeight] = useState(3);
  const [boxWidth, setBoxWidth] = useState(3);

  return (
    <>
      <label for="height">Box height</label>
      <input
        name="height"
        type="number"
        min="2"
        value={boxHeight}
        onChange={(e) => setBoxHeight(e.target.value)}
      />

      <label for="width">Box width</label>
      <input
        name="width"
        type="number"
        min="2"
        value={boxWidth}
        onChange={(e) => setBoxWidth(e.target.value)}
      />

      <Sudoku boxHeight={boxHeight} boxWidth={boxWidth} />
    </>
  );
}

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

  const styles = css({
    display: "grid",
    gridTemplateColumns: `repeat(${size}, 40px)`,
    gridGap: "2px",
    position: 'relative',
  });

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
    <div class={styles}>
      {_.range(1, size + 1).map((row) =>
        _.range(1, size + 1).map((column) => (
          <Cell updateCell={_.partial(updateCell, row, column)} />
        ))
      )}
    </div>
  );
};



export default App;
