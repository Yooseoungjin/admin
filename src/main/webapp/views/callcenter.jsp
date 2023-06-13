<%--jsp 작성을 위해 아래 3줄은 기입.--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--JSTL : 통화 날짜를 표현하게 해주는 문법--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    #all {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid red;
    }

    #me {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid blue;
    }

    #to {
        width: 400px;
        height: 200px;
        overflow: auto;
        border: 2px solid green;
    }
</style>

<script>
    let callcenter = {
        id:null, // 관리자 id
        stompClient:null, // ws와의 커넥션 준비
        init:function(){
            this.id = $('#adm_id').text(); // 관리자 id 가져오기.
            $("#connect").click(function() { // 연결
                callcenter.connect();
            });
            $("#disconnect").click(function() { // 연결해제
                callcenter.disconnect();
            });

            $("#sendto").click(function() { // 버튼3
                callcenter.sendTo();
            });
        },
        connect:function(){ // 연결
            var sid = this.id; // 관리자 id
            var socket = new SockJS('${adminserver}/ws'); // 관리자의 ws 서버로 접속하려 한다.
            this.stompClient = Stomp.over(socket);

            this.stompClient.connect({}, function(frame) { // ws 에 connect(접속) 완료.
                callcenter.setConnected(true);
                console.log('Connected: ' + frame);

                this.subscribe('/send/to/'+sid, function(msg) { // subscribe : 접속완료 즉시 받을준비.
                    $('#target').val(  JSON.parse(msg.body).receiveid ); // 답장 편하게 하도록, input창에 받을사람의 id디폴트로 넣어줘
                    $("#to").prepend(
                        "<h4>" + JSON.parse(msg.body).sendid +":"+ // 메세지 보낸사람의 id를 채팅창에 입력
                        JSON.parse(msg.body).content1
                        + "</h4>");
                });
            });
        },
        disconnect:function(){
            if (this.stompClient !== null) {
                this.stompClient.disconnect();
            }
            callcenter.setConnected(false);
            console.log("Disconnected");
        },
        setConnected:function(connected){
            if (connected) {
                $("#status").text("Connected");
            } else {
                $("#status").text("Disconnected");
            }
        },
        sendTo:function(){ // 특정인에게 msg
            var msg = JSON.stringify({
                'sendid' : this.id,
                'receiveid' : $('#target').val(),
                'content1' : $('#totext').val()
            });
            this.stompClient.send('/receiveto', {}, msg); //receiveto : admin의 msg컨트롤러가 처리
        }
    };
    $(function(){
        callcenter.init();
    })

</script>
<!-- Begin Page Content -->
<div class="container-fluid">

    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800">Callcenter</h1>

    <!-- DataTales Example -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Callcenter</h6>
        </div>
        <div class="card-body">
            <div id="container"></div>
            <div class="col-sm-5">
                <h1>callcenter</h1>
                <h1 id="adm_id">${loginadm.id}</h1>
                <H1 id="status">Status</H1>
                <button id="connect">Connect</button>
                <button id="disconnect">Disconnect</button>

                <h3>To</h3>
                <input type="text" id="target"> <%-- 메세지 보낸사람의 id가 표시되는 공간 --%>

                <input type="text" id="totext"><button id="sendto">Send</button>
                <div id="to"></div>

            </div>
        </div>
    </div>
    <!-- /.container-fluid -->
</div>