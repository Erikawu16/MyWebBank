package mvc.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.protobuf.Option;

import mvc.bean.Currency;
import mvc.bean.User;
import mvc.enums.LoginStatus;
import mvc.enums.UserDataCheck;

public interface LoginService {

	public LoginStatus isValidUser(String userId,String password,String code,String sessionCode) throws Exception;
	// 產生一個驗證碼 code
	public void getCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException;

	
	public Currency getHomePageJPY();
	public Currency getHomePageUSD();
	public Currency getHomePageCNY();
	
	
	public LoginStatus isValidManager(String managername, String password);
	public void addBasicModel(Model model);
	
	boolean isRegistDataPass(@Valid User user,BindingResult result, Model model) throws Exception;
	UserDataCheck isEmailValid(String userId,String email ); 
	public String sendOTP(String userId);
	boolean isOTPValidUser( String validcode, String OTPcode) ;
}

