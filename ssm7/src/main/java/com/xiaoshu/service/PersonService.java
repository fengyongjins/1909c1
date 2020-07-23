package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.PersonExample;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	UserMapper userMapper;
	@Autowired
	CompanyMapper cm;
	@Autowired
	PersonMapper pm;

	// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addPerson(Person p)  throws Exception {
		pm.insert(p);
	};

	// 修改
	public void updatePerson(Person p) throws Exception {
		pm.updateByPrimaryKey(p);
	};

	// 删除
	public void delete(Integer id) throws Exception {
		pm.deleteByPrimaryKey(id);
	};

	// 登录
	public User loginUser(User user) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPasswordEqualTo(user.getPassword()).andUsernameEqualTo(user.getUsername());
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过用户名判断是否存在，（新增时不能重名）
	public Person findByName(String pName) throws Exception {
		PersonExample example = new PersonExample();
		 com.xiaoshu.entity.PersonExample.Criteria criteria = example.createCriteria();
		    criteria.andPNameEqualTo(pName);
		List<Person> pList = pm.selectByExample(example);
		return pList.isEmpty()?null:pList.get(0);
	};

	// 通过角色判断是否存在
	public User existUserWithRoleId(Integer roleId) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleidEqualTo(roleId);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	}

	public PageInfo<Person> findPage(Person p, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		
		List<Person> pList = pm.findAll(p);
		   List<Person> findAll = pm.findAll(null);
		   System.out.println(findAll);
		PageInfo<Person> pageInfo = new PageInfo<Person>(pList);
		return pageInfo;
	}

	public List<Company> findCompany() {
		// TODO Auto-generated method stub
		return cm.selectAll();
	}

	public List<Person> findAllPerson() {
		// TODO Auto-generated method stub
		return pm.findAll(null);
	}


}
