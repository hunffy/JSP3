package model;
 
/*
 * Bean 클래스 : 	private인 변수와 public인 멤버메서드(getter,setter)로
 * 				이루어져 있는 클래스
 *  getId()	=> getter
 *  		   id 값 : get 프로퍼티
 *  setId(String id) => setter
 *  			id : set 프로퍼티
 *  
 *   getXxx => xxx getProperty의 값 getAbc Abc가 프로퍼티값이지만 첫자를 결과적으로 소문자로바꾼값이 프로퍼티값 abc
 *   setXxx => xxx setProperty의 값 
 */
public class Member {
	private String id;
	private String pass;
	private String name;
	private int gender;
	private String tel;
	private String email;
	private String picture;
	//getter, setter
	public String getId() { //getProperty : id
		return id;
	}
	public void setId(String id) { //setProperty : id
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}
