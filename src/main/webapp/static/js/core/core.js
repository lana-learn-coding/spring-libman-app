// Origin init script, should not defer loaded
// Defer script call hack
(() => {
  const queue = [];
  window.defer = (func) => {
    queue.push(func);
  };
  document.addEventListener('DOMContentLoaded', () => {
    while (queue.length) queue.shift().call();
  });
})();

// Initial setup
defer(() => {
  up.fragment.config.runScripts = true;

  up.on('up:fragment:inserted', (e) => {
    feather.replace();
    _hyperscript.processNode(e.target);
  });

  up.macro('[up-nav=include-query]', (e) => {
    e.querySelectorAll('a').forEach(a => {
      const href = a.href || a.getAttribute('up-href');
      if (!href) return;
      const alias = a.getAttribute('up-alias') || '';
      a.setAttribute('up-alias', alias + ' ' + href + '?*');
    });
  });

  up.compiler('#notify-container', (toastContainer) => {
    const toastList = document.querySelectorAll('#notify-hungry .toast');
    if (toastList.length && toastContainer) {
      toastList.forEach(toast => {
        toastContainer.appendChild(toast);
        bootstrap.Toast.getOrCreateInstance(toast).show();
      });
    }
  });

  up.compiler('.loader-wrapper', (loader) => {
    if (loader) {
      loader.style.transition = 'opacity 1.6s';
      let timeoutRemove = null;
      const timeOutOpacity = setTimeout(() => {
        loader.style.opacity = '0';
        timeoutRemove = setTimeout(() => {
          loader.remove();
        }, 1300);
      }, 100);
      return () => {
        clearTimeout(timeoutRemove);
        clearTimeout(timeOutOpacity);
      };
    }
  });
});
