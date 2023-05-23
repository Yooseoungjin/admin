<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>

<script>
    let cust_detail = {
        init:function (){
            $('#update_btn').click(function (){
                var c = confirm("수정할까유?");
                if(c == true){
                    location.href="/cust/updateimpl?id=${gcust.id}";
                    cust_detail.send();
                }

            });
            $('#delete_btn').click(function (){
                var c = confirm("정말 삭제하겠습니까?");
                if(c == true){
                    location.href="/cust/deleteimpl?id=${gcust.id}";
                }
            });
        },
        send:function(){
            $('#detail_form').attr({
                method:'post',
                action:'/cust/updateimpl'
            });
            $('#detail_form').submit();
        }
    }

    $(function (){
        cust_detail.init();
    })
</script>

<div class="container-fluid">
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800"> Cust detail</h1>
    <!-- DataTales Example -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">DataTables Example</h6>
        </div>
        <div class="card-body">
            <div id="container">
                <form id="detail_form" class="form-horizontal well">
                    <div class="form-group">
                        <input type="text" class="form-control" id="id" name="id" value="${gcust.id}" readonly>
                    </div>
                    <div class="col-sm-10">
                        <span id="check_id" class="bg-danger"></span>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="pwd" name="pwd" >
                    </div>
                    <div class="form-group">
                        <input type="name" class="form-control" id="name" name="name" value="${gcust.name}" >
                    </div>

                    <div>
                        <button type="button" id="update_btn" class="btn btn-primary">Update</button>
                        <button type="button" id="delete_btn" class="btn btn-primary">Delete</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>