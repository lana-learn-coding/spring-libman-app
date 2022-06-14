// Origin init script, should not defer loaded
// Defer script call hack
(() => {
  const queue = [];
  window.defer = (func) => {
    queue.push(func);
  };
  document.addEventListener('DOMContentLoaded', () => {
    // feather icon
    feather.replace();

    // Auto active with query for unpoly
    document.querySelectorAll('[up-nav=include-query]').forEach(e => {
      e.querySelectorAll('a').forEach(a => {
        const href = a.href || a.getAttribute('up-href');
        if (!href) return;
        const alias = a.getAttribute('up-alias') || '';
        a.setAttribute('up-alias', alias + ' ' + href + '?*');
      });
    });

    up.on('up:fragment:inserted', () => {
      feather.replace();

      const toast = document.querySelector('#notify-container > .toast');
      if (toast) bootstrap.Toast.getOrCreateInstance(toast).show();
    });

    while (queue.length) queue.shift().call();
  });
})();
