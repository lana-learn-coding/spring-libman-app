$('.loader-wrapper').fadeOut('slow', function () {
  $(this).remove();
});

$('.mode').on('click', function () {
  $('.mode i').toggleClass('fa-moon-o').toggleClass('fa-lightbulb-o');
  $('body').toggleClass('dark-only');
  localStorage.setItem('body', 'dark-only');
});
