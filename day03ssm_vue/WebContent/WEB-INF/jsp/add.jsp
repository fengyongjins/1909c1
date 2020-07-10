<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/vue.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/axios-0.18.0.js"></script>
</head>
<body>
  <form action="" id="fid">
   姓名： <input v-model="student.sname"><br>
  性别：  <input type="radio" v-model="student.sex" value="男">男
    <input type="radio" v-model="student.sex" value="女">女<br>
图片：<input type="file"><br>
    生日：<input type="date" v-model="student.birthday"><br>
  班级：  <select v-model="student.cid" >
  <option v-for="clazz in clist" :value="clazz.cid" v-text="clazz.cname"></option>
  </select><br>
   <input type="button" @click="add" value="添加"> 
  </form>
  <script type="text/javascript">
  var form=new Vue({
	  el:"#fid",
	  data:{
		  clist:[],
		  student:{}
	  },
		created(){
			axios.post("${pageContext.request.contextPath}/findCalzz.action").then(function(res){				
				form.clist = res.data;
			})
		},
 	   methods:{
		  add(){
			  axios.post("${pageContext.request.contextPath}/add.action",form.student).then(function(res){
					 if(res.data>0){
						 location.href="${pageContext.request.contextPath}/toshow.action"
					 };
					
				})  
		  }
		
	  }  
	  
  })
  
  </script>
</body>
</html>