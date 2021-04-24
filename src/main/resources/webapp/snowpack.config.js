/** @type {import("snowpack").SnowpackUserConfig } */

const proxy = require("http2-proxy");

module.exports = {
  mount: {
    public: "/",
    src: "/dist",
  },
  plugins: [
    /* ... */
  ],
  routes: [
    {
      src: "/solve",
      dest: (req, res) =>
        proxy.web(req, res, {
          hostname: "localhost",
          port: 7000,
        }),
    },
  ],
  optimize: {
    /* Example: Bundle your final build: */
    // "bundle": true,
  },
  devOptions: {
    /* ... */
  },
  buildOptions: {
    /* ... */
  },
};
