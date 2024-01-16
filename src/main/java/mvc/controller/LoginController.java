package mvc.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import mvc.bean.Manager;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;
import mvc.enums.LoginStatus;
import mvc.enums.UserDataCheck;
import mvc.service.LoginService;
import mvc.service.LoginServiceImpl;
import mvc.util.GMail;
import mvc.util.KeyUtil;

@Controller
@RequestMapping("/mybank")
public class LoginController {

	@Autowired
	private DataDao dataDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ManagerDao managerDao;

	@Autowired
	private LoginService loginService;

	// 登入首頁
	@GetMapping(value = { "/login", "/", "/login/" })
	public String loginPage(Model model) {
		model.addAttribute("currencyJPY", loginService.getHomePageJPY());
		model.addAttribute("currencyUSD",loginService.getHomePageUSD());
		model.addAttribute("currencyCNY", loginService.getHomePageCNY());
		
		
		return "login";
	}

	// 產生一個驗證碼 code
	@GetMapping("/getcode")
	private void getCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
		
		loginService.getCodeImage(session, response);
	}

	@PostMapping("/login")
	public String customerlogin(@RequestParam("userId") String userId, @RequestParam("password") String password,
			@RequestParam("code") String code, HttpSession session, Model model) throws Exception {

		LoginStatus loginStatus = loginService.isValidUser(userId, password, code, session.getAttribute("code") + "");
		model.addAttribute("code", code);
		model.addAttribute("sessionCode", session.getAttribute("code"));
		
		if (loginStatus == LoginStatus.SUCCESS) {
			User user = userDao.findUserByUserId(userId).get();
			session.setAttribute("user", user); // 將 user 物件放入到 session 變數中
			return "redirect:/mvc/mybank/customer/myaccount"; // OK, 導向前台首頁
		} else if (loginStatus == LoginStatus.CODE_ERROR) {
			model.addAttribute("loginMessage", "驗證碼錯誤");
		} else if (loginStatus == LoginStatus.PASSWORD_ERROR) {
			model.addAttribute("loginMessage", "密碼錯誤");
		} else if (loginStatus == LoginStatus.IN_REVIEW) {
			model.addAttribute("loginMessage", "此帳號尚未審核通過");
		} else if (loginStatus == LoginStatus.NO_USER) {
			model.addAttribute("loginMessage", "無此使用者");
		}
		session.invalidate();
		return "/login";
	}

	// 會員登出處理
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/mvc/mybank/login";
	}

	// 切換後台登入
	@GetMapping(value = { "/login_manager" })
	public String loginswitch() {
		return "manager/login_manager";
	}

	// 後台登入處理
	@PostMapping("/login_manager")
	public String managerLogin(@RequestParam("managername") String managername,
			@RequestParam("password") String password, HttpSession session, Model model) {
		LoginStatus managerLoginStatus = loginService.isValidManager(managername, password);
		if (managerLoginStatus == LoginStatus.SUCCESS) {
			Manager manager = managerDao.findManagerByManagerName(managername).get();
			session.setAttribute("manager", manager);
			return "redirect:/mvc/mybank/manager/pending";

		} else if (managerLoginStatus == LoginStatus.NO_USER) {
			model.addAttribute("loginMessage", "無此員工");
		} else if (managerLoginStatus == LoginStatus.PASSWORD_ERROR) {
			model.addAttribute("loginMessage", "密碼錯誤");
		}
		session.invalidate();
		return "/manager/login_manager";
	}

	// 切換註冊頁面
	@GetMapping("/regist")
	public String regist(@ModelAttribute User user, Model model) {
		loginService.addBasicModel(model);
		return "regist/regist_page";
	}

	// 註冊功能
	@PostMapping("/regist")
	public String registuser(@Valid User user, BindingResult result, Model model) throws Exception {
		if (loginService.isRegistDataPass(user, result, model)) {
			return "redirect:/mvc/mybank/finish";
		}
		return "/regist/regist_page";
	}

	// 進入註冊完成介面
	@GetMapping("/finish")
	public String registfinish() {
		return "/regist/regist_finish";
	}

	// 進入忘記密碼頁面
	@GetMapping("/forgot_password")
	public String forgot_password() {
		return "/regist/forgot_password";
	}

	// 發送OTP與進入OTP密碼頁面
	@PostMapping("/otpcomfirm")
	public String otpcomfirm(@RequestParam("userId") String userId, @RequestParam("email") String email, Model model) {
		UserDataCheck emailCheck = loginService.isEmailValid(userId, email);
		if (emailCheck == UserDataCheck.SUCCESS) {
			loginService.sendOTP(userId);
			return "/regist/forgot_password_login";
		} else if (emailCheck == UserDataCheck.EMAIL_ERROR) {
			model.addAttribute("errorMessage", "信箱錯誤");
		} else if (emailCheck == UserDataCheck.NO_USER) {
			model.addAttribute("errorMessage", "無此用戶");
		}
		return "/regist/forgot_password";
	}

	// OTP密碼必對後登入
	@PostMapping("/otpsend")
	public String otpsend(@RequestParam("userId") String userId, @RequestParam("validcode") String validcode,
			@RequestParam("OTPcode") String OTPcode, Model model, HttpSession session) {
		if (loginService.isOTPValidUser(validcode, OTPcode)) {
			User user = userDao.findUserByUserId(userId).get();
			session.setAttribute("user", user);
			return "redirect:/mvc/mybank/customer/myaccount";
		}
		return "/regist/forgot_password_login";
	}
}
