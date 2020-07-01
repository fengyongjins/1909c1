package cn.jiyun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jiyun.mapper.StudentMapper;
import cn.jiyun.pojo.Student;

@Service
public class StudentService {
        @Autowired
 private  StudentMapper mapper;

		public List<Student> findAll() {
			// TODO Auto-generated method stub
			return mapper.findAll();
		}
}
