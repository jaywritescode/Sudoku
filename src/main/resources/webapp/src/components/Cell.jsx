import React from "react";
import { cx } from "@emotion/css";

import "./Cell.css";

const Cell = (props) => {
  const { updateCell, classNames } = props;

  return (
    <div class={cx("aspect-ratio-box", "cell", classNames)}>
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
