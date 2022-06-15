(() => {
  $('.mobile-toggle').click(function () {
    $('.nav-menus').toggleClass('open');
  });
  $('.mobile-toggle-left').click(function () {
    $('.left-header').toggleClass('open');
  });
})();

(() => {
  const $body = $('body');
  $body.toggleClass('dark-only', localStorage.getItem('body-dark') === 'true');
  $('.mode').on('click', function () {
    $('.mode i').toggleClass('fa-moon-o').toggleClass('fa-lightbulb-o');
    $body.toggleClass('dark-only');
    localStorage.setItem('body-dark', $body.hasClass('dark-only') ? 'true' : 'false');
  });
})();
