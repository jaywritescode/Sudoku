import React, {useState, useEffect} from 'react';
import _ from 'lodash';

function App() {

  const [boxHeight, setBoxHeight] = useState(3);
  const [boxWidth, setBoxWidth] = useState(3);

  return (
    <>
      <label for="height">
        Box height
      </label>
      <input name="height" type="number" min="2" value={boxHeight} onChange={(e) => setBoxHeight(e.target.value)} />

      <label for="width">
        Box width
      </label>
      <input name="width" type="number" min="2" value={boxWidth} onChange={(e) => setBoxWidth(e.target.value)} />



      <p>box width is {boxWidth}</p>
      <p>box height is {boxHeight}</p>
    </>
  )
}





/*
function App() {
  // Create the count state.
  const [count, setCount] = useState(0);
  // Update the count (+1 every second).
  useEffect(() => {
    const timer = setTimeout(() => setCount(count + 1), 1000);
    return () => clearTimeout(timer);
  }, [count, setCount]);
  // Return the App component.
  return (
    <div className="App">
      <header className="App-header">
        <p>
          Page has been open for <code>{count}</code> seconds.
        </p>
      </header>
    </div>
  );
}
*/

export default App;