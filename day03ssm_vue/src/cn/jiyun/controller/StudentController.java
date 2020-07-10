package cn.jiyun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jiyun.pojo.Clazz;
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
	@RequestMapping("toadd")
	public String toadd(){
		return "add";	
	}
	@RequestMapping("findCalzz")
	@ResponseBody
	public List<Clazz>findCalzz(){
		List<Clazz>clist=stu.findCalzz();
		
		return clist;
		
	}
	@RequestMapping("add")
	@ResponseBody
	public int add(@RequestBody Student student){
		 int i = stu.add(student);
		return i;
		
	}
	//修改
	@ResponseBody
	@RequestMapping("update")
	public int update(@RequestBody Student student){
		int i = stu.update(student);
		return i;
	}
	//删除
	@ResponseBody
	@RequestMapping("delStu")
	public int delStu(@RequestBody Integer[] ids){
		int i = stu.delStu(ids);
		return i;
	}
}
