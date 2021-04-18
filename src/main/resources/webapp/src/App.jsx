import React, { useState } from "react";

import Sudoku from "./components/Sudoku";

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

export default App;
