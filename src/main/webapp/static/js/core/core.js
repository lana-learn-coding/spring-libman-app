// Origin init script, should not defer loaded

// Defer script call hack
const queue = window._deferQueue || [];
window.defer = (func) => {
  queue.push(func);
};
document.addEventListener('DOMContentLoaded', () => {
  // Auto active with query for unpoly
  document.querySelectorAll('[up-nav=include-query]').forEach(e => {
    e.querySelectorAll('a').forEach(a => {
      const href = a.href || a.getAttribute('up-href');
      if (!href) return;
      const alias = a.getAttribute('up-alias') || '';
      a.setAttribute('up-alias', alias + ' ' + href + '?*');
    });
  });

  while (queue.length) queue.shift().call();
});
