package test;


import java.util.Date;
public class Student {

    private Integer sid;

    private String sname;

    private String sex;

    private String img;

    private String hobby;

    private Date birthday;

    private Integer cid;

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Student(Integer sid, String sname, String sex, String img, String hobby, Date birthday, Integer cid) {
		super();
		this.sid = sid;
		this.sname = sname;
		this.sex = sex;
		this.img = img;
		this.hobby = hobby;
		this.birthday = birthday;
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "Student [sid=" + sid + ", sname=" + sname + ", sex=" + sex + ", img=" + img + ", hobby=" + hobby
				+ ", birthday=" + birthday + ", cid=" + cid + "]";
	}
    

   
}