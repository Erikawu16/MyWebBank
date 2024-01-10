package mvc.bean;

import java.util.List;

import lombok.Data;

@Data
public class Manager {
	private Integer managerId; // 使用者代號
	private String managername; // 使用者名稱
	private String password; // 使用者密碼
	private Integer levelId;
	private List<Service> services;
	
	
}

