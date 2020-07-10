package cn.jiyun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jiyun.mapper.StudentMapper;
import cn.jiyun.pojo.Clazz;
import cn.jiyun.pojo.Student;

@Service
public class StudentService {
        @Autowired
 private  StudentMapper mapper;

		public List<Student> findAll() {
			// TODO Auto-generated method stub
			return mapper.findAll();
		}

		public List<Clazz> findCalzz() {
			// TODO Auto-generated method stub
			return mapper.findCalzz();
		}

		public int add(Student student) {
			// TODO Auto-generated method stub
			return mapper.add(student);
		}

		public int update(Student student) {
			// TODO Auto-generated method stub
			return mapper.update(student);
		}

		public int delStu(Integer[] ids) {
			// TODO Auto-generated method stub
			return mapper.delStu(ids);
		}

}
