<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/static/js/datepicker/daterange-picker/daterange-picker.css"/>">

<script>
    scoped(() => {
        if ($.fn.daterangepicker) return;
        const queue = [];
        window.bindDateRangePicker = (selector, opt, cb) => {
            if ($.fn.daterangepicker) {
                $(selector).daterangepicker(opt, cb);
                return;
            }
            queue.push(() => $(selector).daterangepicker(opt, cb));
        };
        inject('/static/js/datepicker/daterange-picker/moment.min.js');
        inject('/static/js/datepicker/daterange-picker/daterangepicker.js').then(() => {
            while (queue.length) queue.shift().call();
        });
    });
</script>

