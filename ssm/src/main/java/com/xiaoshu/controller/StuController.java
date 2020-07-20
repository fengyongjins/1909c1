package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Clazz;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StuService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping("stu")
public class StuController extends LogController{
	static Logger logger = Logger.getLogger(StuController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService ;
	@Autowired
	private StuService ss ;
	
	@Autowired
	private OperationService operationService;
	
	//导入
		@RequestMapping("inStu")
	public void inStu(HttpServletResponse response,MultipartFile  file) throws Exception{
			JSONObject result=new JSONObject();
			try {
				 InputStream is = file.getInputStream();
				//1.创建工作簿
	
				//2.读取sheet页
				XSSFSheet sheet = workbook.getSheetAt(0);
				//3.获取行
				int lastRowNum = sheet.getLastRowNum();
				ArrayList<Student> list = new ArrayList<Student>();
				for (int i = 1; i <= lastRowNum; i++) {
					XSSFRow row = sheet.getRow(i);
					Student student = new Student();
					//student.setSid((int)row.getCell(0).getNumericCellValue());
					student.setSname(row.getCell(1).getStringCellValue());
					student.setSex(row.getCell(2).getStringCellValue());
					String bir = row.getCell(3).getStringCellValue();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					student.setBirthday(simpleDateFormat.parse(bir));
					student.setHobby(row.getCell(4).getStringCellValue());  
					if("H1909A".equals(row.getCell(5).getStringCellValue())){
						student.setCid(1);
					}else if("H1909B".equals(row.getCell(5).getStringCellValue())){
						student.setCid(2);
					}else{
						student.setCid(3);
					}
					ss.addStu(student);
					
				}
				result.put("success", true);	
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("保存学生信息错误",e);
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
		//导出
		@RequestMapping("outStu")
		public void outStu(HttpServletRequest request,HttpServletResponse response){
			JSONObject result = new JSONObject();
			try {
				String time = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
			    String excelName = "学生信息"+time;
				Log log = new Log();
				List<Student> list = ss.findAllStu();
				System.out.println(list);
				String[] handers = {"编号","姓名","性别","生日","爱好","班级"};
				// 1导入硬盘
				ExportExcelToDisk(request,handers,list, excelName);
				
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("", "对不起，导出失败");
			}
			WriterUtil.write(response, result.toString());
		}
		
		
		
		// 导出到硬盘
		@SuppressWarnings("resource")
		private void ExportExcelToDisk(HttpServletRequest request,
				String[] handers, List<Student> list, String excleName) throws Exception {
			
			try {
				HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
				HSSFSheet sheet = wb.createSheet("操作记录备份");//第一个sheet
				HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
				rowFirst.setHeight((short) 500);
				for (int i = 0; i < handers.length; i++) {
					sheet.setColumnWidth((short) i, (short) 4000);// 设置列宽
				}
				//写标题了
				for (int i = 0; i < handers.length; i++) {
				    //获取第一行的每一个单元格
				    HSSFCell cell = rowFirst.createCell(i);
				    //往单元格里面写入值
				    cell.setCellValue(handers[i]);
				}
				for (int i = 0;i < list.size(); i++) {
				    //获取list里面存在是数据集对象
				    Student stu = list.get(i);
				    //创建数据行
				    HSSFRow row = sheet.createRow(i+1);
				    //设置对应单元格的值
				    row.setHeight((short)400);   // 设置每行的高度
				    //"序号","操作人","IP地址","操作时间","操作模块","操作类型","详情"
				    row.createCell(0).setCellValue(list.get(i).getSid());
					row.createCell(1).setCellValue(list.get(i).getSname());
					row.createCell(2).setCellValue(list.get(i).getSex());
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");					
					row.createCell(3).setCellValue(simpleDateFormat.format(list.get(i).getBirthday()));
					row.createCell(4).setCellValue(list.get(i).getHobby());
					row.createCell(5).setCellValue(list.get(i).getClazz().getCname());
				}
				//写出文件（path为文件路径含文件名）
					OutputStream os;
					File file = new File("d:/"+excleName+".xls");
					
					if (!file.exists()){//若此目录不存在，则创建之  
						file.createNewFile();  
						logger.debug("创建文件夹路径为："+ file.getPath());  
		            } 
					os = new FileOutputStream(file);
					wb.write(os);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
		}
	@RequestMapping("stuIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Clazz> cList = ss.findClazz();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("cList", cList);
		return "stu";
	}
	
	
	@RequestMapping(value="stuList",method=RequestMethod.POST)
	public void userList(HttpServletRequest request,Student stu,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			/*User user = new User();
			String username = request.getParameter("username");
			String roleid = request.getParameter("roleid");
			String usertype = request.getParameter("usertype");*/
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			/*if (StringUtil.isNotEmpty(username)) {
				user.setUsername(username);
			}
			if (StringUtil.isNotEmpty(roleid) && !"0".equals(roleid)) {
				user.setRoleid(Integer.parseInt(roleid));
			}
			if (StringUtil.isNotEmpty(usertype)) {
				user.setUsertype(usertype.getBytes()[0]);
			}*/
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Student> stuList= ss.findStuPage(stu,pageNum,pageSize,ordername,order);
			
		/*	request.setAttribute("username", username);
			request.setAttribute("roleid", roleid);
			request.setAttribute("usertype", usertype);*/
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",stuList.getTotal() );
			jsonObj.put("rows", stuList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("学生展示错误",e);
			throw e;
		}
	}
	
	
	
	
	// 新增或修改
	@RequestMapping("reserveStu")
	public void reserveUser(HttpServletRequest request,Student stu,String[]hobbys,HttpServletResponse response,MultipartFile  pic){
		
		Integer sId = stu.getSid();
		System.out.println(sId);
		System.out.println(stu);
		String hobby = StringUtils.join(hobbys,"," );
		stu.setHobby(hobby);
		JSONObject result=new JSONObject();
		String oldName = pic.getOriginalFilename();
		
		try {
			if (sId!= null) {   // userId不为空 说明是修改
				Student student = ss.findByName(stu.getSname());
				if((student!= null && student.getSid().compareTo(sId)==0)||student==null){
					if (oldName!=null&&oldName!="") {
						long timeMillis = System.currentTimeMillis();
						String newName=timeMillis+oldName.substring(oldName.lastIndexOf("."));
						pic.transferTo(new File("e:/photo/"+newName));
						  stu.setImg("/photo/"+newName);
					}
                             ss.updateStu(stu);
					result.put("success", true);
				}else{
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
				
			}else {   // 添加
				if(ss.findByName(stu.getSname())==null){  // 没有重复可以添加
					if (oldName!=null&&oldName!="") {
						long timeMillis = System.currentTimeMillis();
						String newName=oldName.substring(oldName.lastIndexOf("."))+timeMillis;
						pic.transferTo(new File("e:/photo/"+newName));
						  stu.setImg("/photo/"+newName);
					}
					ss.addStu(stu);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存学生信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteStu")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			
			
			String[] ids=request.getParameter("ids").split(",");
			
			for (String id : ids) {
				System.out.println(id);
				ss.deleteStu(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("editPassword")
	public void editPassword(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser.getPassword().equals(oldpassword)){
			User user = new User();
			user.setUserid(currentUser.getUserid());
			user.setPassword(newpassword);
			try {
				userService.updateUser(user);
				currentUser.setPassword(newpassword);
				session.removeAttribute("currentUser"); 
				session.setAttribute("currentUser", currentUser);
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改密码错误",e);
				result.put("errorMsg", "对不起，修改密码失败");
			}
		}else{
			logger.error(currentUser.getUsername()+"修改密码时原密码输入错误！");
			result.put("errorMsg", "对不起，原密码输入错误！");
		}
		WriterUtil.write(response, result.toString());
	}
}
