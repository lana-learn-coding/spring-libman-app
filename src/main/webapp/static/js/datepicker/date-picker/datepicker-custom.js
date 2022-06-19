scoped(() => {
  function initDatePicker() {
    $('.datepicker-here').datepicker({ gotoCurrent: true });
  }

  if ($.fn.datepicker) {
    initDatePicker();
    return;
  }
  inject('/static/js/datepicker/date-picker/datepicker.js').then(() => {
    initDatePicker();
  });
});
