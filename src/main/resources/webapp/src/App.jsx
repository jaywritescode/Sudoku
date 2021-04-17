import React, { useState, useEffect } from "react";
import _ from "lodash";
import { css } from '@emotion/css';

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

  const styles = css({
    display: 'grid',
    gridTemplateColumns: `repeat(${boxHeight * boxWidth}, 1fr)`,
    gridGap: '2px',

    '> div': {
      width: '100%',
      paddingTop: '100%',
      position: 'relative',
    },
  });

  return (
    <div class={styles}>
      {_.range(1, boxHeight * boxWidth + 1).map((row) =>
        _.range(1, boxHeight * boxWidth + 1).map((column) => (
          <div id={`a.${row}.${column}`}>
          </div>
        ))
      )}
    </div>
  );
};

export default App;
