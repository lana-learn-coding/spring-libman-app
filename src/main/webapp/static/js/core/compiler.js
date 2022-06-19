up.fragment.config.runScripts = true;

up.on('up:fragment:inserted', (e) => {
  if (window.feather) feather.replace();
  if (window._hyperscript) _hyperscript.processNode(e.target);
});

up.macro('[up-nav=include-query]', (e) => {
  e.querySelectorAll('a').forEach(a => {
    const href = a.href || a.getAttribute('up-href');
    if (!href) return;
    const alias = a.getAttribute('up-alias') || '';
    a.setAttribute('up-alias', alias + ' ' + href + '?*');
  });
});

up.compiler('#notify-hungry .toast', (toast) => {
  const toastContainer = document.querySelector('#notify-container');
  if (toastContainer) {
    toastContainer.appendChild(toast);
    bootstrap.Toast.getOrCreateInstance(toast).show();
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
