import React from "react";
import { cx } from "@emotion/css";

import "./Cell.css";

const Cell = (props) => {
  const { updateCell, value, classNames } = props;

  return (
    <div className={cx("aspect-ratio-box", "cell", classNames)}>
      <div className="aspect-ratio-box-inside">
        <input
          type="text"
          value={value || ""}
          maxLength="1"
          onInput={(e) => updateCell(e.target.value)}
        />
      </div>
    </div>
  );
};

export default Cell;
