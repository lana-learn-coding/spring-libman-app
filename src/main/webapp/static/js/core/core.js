// Origin init script, should not defer loaded
// Defer script call hack
(() => {
  const queue = [];
  window.defer = (func) => {
    queue.push(func);
  };
  document.addEventListener('DOMContentLoaded', () => {
    // Feather icon initial
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
      renderToast();
      hideWrapper();
    });

    function renderToast() {
      const toastContainer = document.getElementById('notify-container');
      const toastList = document.querySelectorAll('#notify-hungry .toast');
      if (toastList.length && toastContainer) {
        toastList.forEach(toast => {
          toastContainer.appendChild(toast);
          bootstrap.Toast.getOrCreateInstance(toast).show();
        });
      }
    }

    function hideWrapper() {
      const loader = document.querySelector('.loader-wrapper');
      if (loader) {
        loader.style.transition = 'opacity 1.6s';
        setTimeout(() => {
          loader.style.opacity = '0';
          setTimeout(() => {
            loader.remove();
          }, 1500);
        }, 100);
      }
    }

    while (queue.length) queue.shift().call();
  });
})();
