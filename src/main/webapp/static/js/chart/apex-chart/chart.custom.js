scoped(async () => {
  if (window.ApexCharts) return;

  const queue = [];
  window.createApexCharts = async (selector, opt) => {
    const el = typeof selector === 'string' ? document.querySelector(selector) : selector;
    if (window.ApexCharts) {
      return Promise.resolve(new window.ApexCharts(el, opt));
    }
    return new Promise(resolve => queue.push(() => resolve(new window.ApexCharts(el, opt))));
  };

  window.showApexChartsFromData = (selector, options, data = []) => {
    const chartOptions = {
      chart: {
        toolbar: {
          show: false,
        },
        type: 'area',
      },
      series: [{
        data,
      }],
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
      },
      xaxis: {
        show: true,
        type: 'datetime',
        labels: {
          show: true,
        },
        axisBorder: {
          show: true,
        },
      },
      grid: {
        show: false,
        padding: {
          left: 30,
          right: 30,
        },
      },
      fill: {
        type: 'gradient',
        gradient: {
          shade: 'light',
          type: 'vertical',
          shadeIntensity: 0.4,
          inverseColors: false,
          opacityFrom: 0.8,
          opacityTo: 0.2,
          stops: [0, 100],
        },
      },
      colors: [vihoAdminConfig.primary],
      ...options,
    };
    window.createApexCharts(selector, chartOptions).then(c => c.render());
  };

  await inject('/static/js/chart/apex-chart/apex-chart.js');
  while (queue.length) queue.shift().call();
});
