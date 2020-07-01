package cn.jiyun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jiyun.pojo.Student;
import cn.jiyun.service.StudentService;

@Controller
public class StudentController {
	 @Autowired
	 private StudentService stu;
	@RequestMapping("toshow")
	public String toshow(){
		return "show";	
	}
	@RequestMapping("findAll")
	@ResponseBody
	public List<Student>findAll(){
		List<Student>list=stu.findAll();
		System.out.println(list);
		return list;
		
	}
}
