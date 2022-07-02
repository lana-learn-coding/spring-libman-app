scoped(async () => {
  if ($.fn.counterUp) {
    init();
    return;
  }

  function init() {
    $('.counter').each(function () {
      const $el = $(this);
      $el.counterUp({
        delay: 10,
        time: 1000,
        ...$el.data(),
      });
    });
  }

  await inject('/static/js/counter/jquery.waypoints.min.js');
  await inject('/static/js/counter/jquery.counterup.min.js');
  init();
});
