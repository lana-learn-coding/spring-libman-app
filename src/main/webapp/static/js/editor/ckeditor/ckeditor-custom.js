scoped(() => {
  function initCkEditor() {
    const isDark = localStorage.getItem('body-dark') === 'true';
    CKEDITOR.config.bodyClass = isDark ? 'dark-only' : '';
    document.querySelectorAll('[data-editor=true]').forEach(el => {
      if (!el.id) return;
      CKEDITOR.replace(el.id);
    });
  }

  function registerCkEditor() {
    if (window.CKEDITOR) {
      initCkEditor();
      return;
    }
    inject(CKEDITOR_BASEPATH + 'ckeditor.js');
    inject(CKEDITOR_BASEPATH + 'styles.js').then(() => initCkEditor());
  }

  registerCkEditor();
});
