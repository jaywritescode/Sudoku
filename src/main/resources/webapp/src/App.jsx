import React, {useState, useEffect} from 'react';
import _ from 'lodash';

function App() {
  const boxWidth = 3;
  const boxHeight = 3;

  return (
    <>
      {_.range(1, boxWidth * boxHeight + 1).map}
    </>
  )
}

function Cell(props) {
  const { row, column, onChange } = props;

  return (
    <div className="cell">
      <input type="text" onChange={onChange} />
    </div>
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