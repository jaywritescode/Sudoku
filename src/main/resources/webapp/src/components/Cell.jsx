import React from "react";

import "./Cell.css";

const Cell = (props) => {
  const { updateCell } = props;

  return (
    <div class="aspect-ratio-box cell">
      <div class="aspect-ratio-box-inside">
        <input
          type="text"
          maxLength="1"
          onInput={(e) => updateCell(e.target.value)}
        />
      </div>
    </div>
  );
};

export default Cell;
