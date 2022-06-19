// Origin init script, should not defer loaded
(() => {
  window.scoped = (func) => func.call();

  window.inject = (src, options) => {
    options = options ?? {};
    const script = document.createElement('script');
    return new Promise((resolve) => {
      if (options.inline) {
        script.appendChild(document.createTextNode(src));
        document.body.appendChild(script);
        return resolve();
      }

      script.onload = () => resolve();
      script.src = src;
      script.async = false;
      document.body.appendChild(script);
    });
  };

  // Defer script call hack
  const queue = [];
  window.defer = (func) => {
    queue.push(func);
  };

  document.addEventListener('DOMContentLoaded', () => {
    while (queue.length) queue.shift().call();
  });
})();
