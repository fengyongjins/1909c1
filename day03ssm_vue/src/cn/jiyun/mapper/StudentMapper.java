package cn.jiyun.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import cn.jiyun.pojo.Clazz;
import cn.jiyun.pojo.Student;

public interface StudentMapper {

	List<Student> findAll();

	List<Clazz> findCalzz();

	int  add(Student student);

	int update(Student student);

	int delStu( @Param("ids")Integer[] ids);

}
