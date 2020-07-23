package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;

import com.xiaoshu.entity.Company;

import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.PersonService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("stu")
public class PersonController extends LogController{
	static Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	@Autowired
	private PersonService ps;
	
	  //导出
	@RequestMapping("outPerson")
	public void outPerson(HttpServletRequest request,HttpServletResponse response){
		JSONObject result = new JSONObject();
		try {
			String time = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
		    String excelName = "员工信息"+time;
			List<Person>list=ps.findAllPerson();
			String[] handers = {"员工编号","员工名称","员工性别","所属公司","入职时间"};
			// 1导入硬盘
			ExportExcelToDisk(request,handers,list,excelName);
			  
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
			String[] handers, List<Person> list, String excleName) throws Exception {
		
		try {
			HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
			HSSFSheet sheet = wb.createSheet("员工信息");//第一个sheet
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
		     	Person person = list.get(i);
			    //创建数据行
			    HSSFRow row = sheet.createRow(i+1);
			    //设置对应单元格的值
			    row.setHeight((short)400);   // 设置每行的高度
			    //"员工编号","员工名称","员工性别","所属公司","入职时间"
			    row.createCell(0).setCellValue(list.get(i).getId());
			    row.createCell(1).setCellValue(list.get(i).getpName());
			    row.createCell(2).setCellValue(list.get(i).getGender());
			    row.createCell(3).setCellValue(list.get(i).getCompany().getCompanyName());
			     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			    row.createCell(4).setCellValue(dateFormat.format(list.get(i).getOuttime()));
			 
			   
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
  //导入
	@RequestMapping("inPerson")
	public void inPerson(HttpServletRequest request,HttpServletResponse response,MultipartFile file){
		JSONObject result=new JSONObject();
		try {
		      InputStream is = file.getInputStream();   
		        //创建工作簿
		      HSSFWorkbook workbook = new HSSFWorkbook(is);
				//读取sheet页
		      HSSFSheet sheet = workbook.getSheetAt(0);
				//获取行
				int rowNum = sheet.getLastRowNum();
				ArrayList<Person> list = new ArrayList<Person>();
				
				for (int i = 0; i <= rowNum; i++) {
					HSSFRow row = sheet.getRow(i);
					Person p=new Person();
					p.setpName(row.getCell(1).getStringCellValue());
					p.setGender(row.getCell(2).getStringCellValue());
				    if("圆通快递".equals(row.getCell(3).getStringCellValue())){
				    	  p.setCompanyId(1);
				    }else  if("韵达快递".equals(row.getCell(3).getStringCellValue())){
				    	  p.setCompanyId(2);
				    }else  if("中通快递".equals(row.getCell(3).getStringCellValue())){
				    	  p.setCompanyId(3);
				    }else{
				    	p.setCompanyId(4);
				    }
				 
				     String time = row.getCell(4).getStringCellValue();
				     SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				      Date parse = SimpleDateFormat.parse(time);
				      System.out.println(parse);
				  
				        p.setOuttime(parse);
				     ps.addPerson(p);
				     
				}
			result.put("success", true);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	

		
	
	@RequestMapping("stuIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Company> cList = ps.findCompany();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("cList", cList);
		return "person";
	}
	
	
	@RequestMapping(value="pList",method=RequestMethod.POST)
	public void userList(Person p,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Person> pList= ps.findPage(p,pageNum,pageSize,ordername,order);
			
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",pList.getTotal() );
			jsonObj.put("rows", pList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reservePerson")
	public void reserveUser(HttpServletRequest request,Person p,HttpServletResponse response){
		    Integer id = p.getId();
		JSONObject result=new JSONObject();
		try {
			if (id != null) {   // userId不为空 说明是修改
				Person person= ps.findByName(p.getpName());
				if((person != null && person.getId().compareTo(id)==0)||person==null){
	
					ps.updatePerson(p);
					result.put("success", true);
				}else{
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
				
			}else {   // 添加
				if(ps.findByName(p.getpName())==null){  // 没有重复可以添加
					ps.addPerson(p);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("delete")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				ps.delete(Integer.parseInt(id));
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
