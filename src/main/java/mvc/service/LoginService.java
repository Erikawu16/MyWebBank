package mvc.service;

import mvc.enums.LoginStatus;

public interface LoginService {

	public LoginStatus isValidUser(String userId,String password,String code,String sessionCode) throws Exception;
	
}
