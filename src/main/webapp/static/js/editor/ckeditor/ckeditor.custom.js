up.compiler('[data-editor=true]', (el) => {
  if (!el.id) return;
  CKEDITOR.replace(el.id, {
    // on: {
    //   contentDom: function (evt) {
    //     // Allow custom context menu only with table elemnts.
    //     evt.editor.editable().on('contextmenu', function (contextEvent) {
    //       const path = evt.editor.elementPath();
    //       if (!path.contains('table')) {
    //         contextEvent.cancel();
    //       }
    //     }, null, null, 5);
    //   },
    // },
  });
});
