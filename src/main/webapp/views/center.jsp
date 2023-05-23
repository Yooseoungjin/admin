<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
  let websocket_center = {
    stompClient:null,
    init:function(){
      this.connect();
    },
    connect:function(){
      var sid = this.id;
      var socket = new SockJS('${adminserver}/wss');
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        this.subscribe('/sendadm', function(msg) {
          $('#content1_msg').text(JSON.parse(msg.body).content1);
          $('#content2_msg').text(JSON.parse(msg.body).content2);
          $('#content3_msg').text(JSON.parse(msg.body).content3);
          $('#content4_msg').text(JSON.parse(msg.body).content4);
          $('#progress1').css('width',JSON.parse(msg.body).content1/100*100+'%');
          $('#progress1').attr('aria-valuenow',JSON.parse(msg.body).content1/100*100);
          $('#progress2').css('width',JSON.parse(msg.body).content2/1000*100+'%');
          $('#progress2').attr('aria-valuenow',JSON.parse(msg.body).content2/1000*100);
          $('#progress3').css('width',JSON.parse(msg.body).content3/500*100+'%');
          $('#progress3').attr('aria-valuenow',JSON.parse(msg.body).content3/500*100);
          $('#progress4').css('width',JSON.parse(msg.body).content4/10*100+'%');
          $('#progress4').attr('aria-valuenow',JSON.parse(msg.body).content4/10*100);
        });
      });
    }
  };

  let center_chart1 = {
    init:function(){
      $.ajax({
        url:'/chart1',
        success:function (data){
          center_chart1.display1(data);
        }
      });
    },
    display1:function (data){
      Highcharts.chart('container1', {
        chart: {
          type: 'line'
        },
        title: {
          text: 'Monthly Sales'
        },
        subtitle: {
          text: 'Source: ' +
                  '<a href="https://en.wikipedia.org/wiki/List_of_cities_by_average_temperature" ' +
                  'target="_blank">Wikipedia.com</a>'
        },
        xAxis: {
          categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },
        yAxis: {
          title: {
            text: 'Sales'
          }
        },
        plotOptions: {
          line: {
            dataLabels: {
              enabled: true
            },
            enableMouseTracking: false
          }
        },
        series:data
      });

    }
  };
  let center_chart2 = {
    init:function (){
      $.ajax({
        url:'/chart1',
        success:function (data){
          center_chart2.display2(data);
        }
      });
    },
    display2:function (data){
      Highcharts.chart('container2', {
        chart: {
          type: 'column'
        },
        title: {
          text: 'Monthly Sales'
        },
        subtitle: {
          text: 'Source: WorldClimate.com'
        },
        xAxis: {
          categories: [
            'Jan',
            'Feb',
            'Mar',
            'Apr',
            'May',
            'Jun',
            'Jul',
            'Aug',
            'Sep',
            'Oct',
            'Nov',
            'Dec'
          ],
          crosshair: true
        },
        yAxis: {
          min: 0,
          title: {
            text: 'Rainfall (mm)'
          }
        },
        tooltip: {
          headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
          pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
          footerFormat: '</table>',
          shared: true,
          useHTML: true
        },
        plotOptions: {
          column: {
            pointPadding: 0.2,
            borderWidth: 0
          }
        },
        series:data
      });
    }
  };

  $(function(){
    websocket_center.init();

    center_chart1.init();
    center_chart2.init();
    setInterval(center_chart1.init,5000)
    setInterval(center_chart2.init,15000)
  });

</script>
<!-- Begin Page Content -->
<div class="container-fluid">
  <!-- Page Heading -->
  <div class="d-sm-flex align-items-center justify-content-between mb-4">
    <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
    <a href="#" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i
            class="fas fa-download fa-sm text-white-50"></i> Generate Report</a>
  </div>
  <!-- Content Row -->
  <div class="row">
    <!-- Earnings (Monthly) Card Example -->
    <div class="col-xl-3 col-md-6 mb-4">
      <div class="card border-left-success shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Tasks
              </div>
              <div class="row no-gutters align-items-center">
                <div class="col-auto">
                  <div id="content1_msg" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">50%</div>
                </div>
                <div class="col">
                  <div class="progress progress-sm mr-2">
                    <div id="progress1" class="progress-bar bg-success" role="progressbar"
                         style="width: 50%" aria-valuenow="50" aria-valuemin="0"
                         aria-valuemax="100"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-calendar fa-2x text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Earnings (Monthly) Card Example -->
    <div class="col-xl-3 col-md-6 mb-4">
      <div class="card border-left-primary shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Tasks
              </div>
              <div class="row no-gutters align-items-center">
                <div class="col-auto">
                  <div id="content2_msg" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">50%</div>
                </div>
                <div class="col">
                  <div class="progress progress-sm mr-2">
                    <div id="progress2" class="progress-bar bg-primary" role="progressbar"
                         style="width: 50%" aria-valuenow="50" aria-valuemin="0"
                         aria-valuemax="100"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Earnings (Monthly) Card Example -->
    <div class="col-xl-3 col-md-6 mb-4">
      <div class="card border-left-dark shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-dark text-uppercase mb-1">Tasks
              </div>
              <div class="row no-gutters align-items-center">
                <div class="col-auto">
                  <div id="content3_msg" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">50%</div>
                </div>
                <div class="col">
                  <div class="progress progress-sm mr-2">
                    <div id="progress3" class="progress-bar bg-dark" role="progressbar"
                         style="width: 50%" aria-valuenow="50" aria-valuemin="0"
                         aria-valuemax="100"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Pending Requests Card Example -->
    <div class="col-xl-3 col-md-6 mb-4">
      <div class="card border-left-danger shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">Tasks
              </div>
              <div class="row no-gutters align-items-center">
                <div class="col-auto">
                  <div id="content4_msg" class="h5 mb-0 mr-3 font-weight-bold text-gray-800">50%</div>
                </div>
                <div class="col">
                  <div class="progress progress-sm mr-2">
                    <div id="progress4" class="progress-bar bg-danger" role="progressbar"
                         style="width: 50%" aria-valuenow="50" aria-valuemin="0"
                         aria-valuemax="100"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-comments fa-2x text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Content Row -->
  <div class="row">
    <!-- Area Chart -->
    <div class="col-xl-6 col-lg-7">
      <div class="card shadow mb-4">
        <!-- Card Header - Dropdown -->
        <div
                class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">Earnings Overview</h6>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div class="chart-area" id="container1">

          </div>
        </div>
      </div>
    </div>
    <!-- Pie Chart -->
    <div class="col-xl-6 col-lg-7">
      <div class="card shadow mb-4">
        <!-- Card Header - Dropdown -->
        <div
                class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
          <h6 class="m-0 font-weight-bold text-primary">Revenue Sources</h6>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div class="chart-area" id="container2">

          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Content Row -->
</div>
<!-- /.container-fluid -->