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
import javax.mail.*;
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
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import mvc.bean.Manager;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;
import mvc.service.GMail;
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

	@GetMapping("/getcode")
	private void getCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
		// 產生一個驗證碼 code
		Random random = new Random();
		String code1 = String.format("%c", (char) (random.nextInt(26) + 65));
		String code2 = String.valueOf(random.nextInt(10));
		String code3 = String.format("%c", (char) (random.nextInt(26) + 65));
		String code4 = String.valueOf(random.nextInt(10));

		String code = code1 + code2 + code3 + code4;
		session.setAttribute("code", code);

		// Java 2D 產生圖檔
		// 1. 建立圖像暫存區
		BufferedImage img = new BufferedImage(200, 80, BufferedImage.TYPE_INT_BGR);
		// 2. 建立畫布
		Graphics g = img.getGraphics();
		// 3. 設定顏色
		g.setColor(Color.LIGHT_GRAY);
		// 4. 塗滿背景
		g.fillRect(0, 0, 200, 80);
		// 5. 設定文字顏色
		g.setColor(Color.WHITE);
		// 6. 設定自型
		g.setFont(new Font("新細明體", Font.PLAIN, 50));
		// 7. 繪字串
		g.drawString(code, 60, 55); // code, x, y
		// 8. 干擾線
		g.setColor(Color.WHITE);
		for (int i = 0; i < 15; i++) { // 增加干擾線的數量為 15 條
			int x1 = random.nextInt(200); // 使干擾線跨越整個圖像的寬度
			int y1 = random.nextInt(80); // 使干擾線跨越整個圖像的高度
			int x2 = random.nextInt(200); // 使干擾線跨越整個圖像的寬度
			int y2 = random.nextInt(80); // 使干擾線跨越整個圖像的高度
			g.drawLine(x1, y1, x2, y2);
		}

		// 設定回應類型
		response.setContentType("image/png");

		// 將影像串流回寫給 client
		ImageIO.write(img, "PNG", response.getOutputStream());
	}
	@ApiOperation("登入首頁")
	// 登入首頁
	@GetMapping(value = { "/login", "/", "/login/" })
	public String loginPage(Model model) {
	//更新匯率表
		dataDao.updateCurrency();
		//日幣
		model.addAttribute("currencyJPY", userDao.getCurrencyById(4).get());
		//美金
		model.addAttribute("currencyUSD", userDao.getCurrencyById(2).get());
		//人民幣
		model.addAttribute("currencyCNY", userDao.getCurrencyById(10).get());
		return "login";
	}

	// 前台登入處理
	@PostMapping("/login")
	public String customerlogin(@RequestParam("userId") String userId, @RequestParam("password") String password,
			@RequestParam("code") String code, HttpSession session, Model model) throws Exception {

		// 比對驗證碼
//		if(!code.equals(session.getAttribute("code")+"")) {
//			session.invalidate(); // session 過期失效
//			model.addAttribute("loginMessage", "驗證碼錯誤");
//			return "/login";
//		}

// 根據 username 查找 user 物件
		Optional<User> userOpt = userDao.findUserByUserId(userId);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// 將 password 進行 AES 加密
			// -------------------------------------------------------------------
			final String KEY = KeyUtil.getSecretKey();
			SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, password);
			String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);

			System.out.println(user.getPassword());
			System.out.println(encryptedPasswordECBBase64);
			if (user.getStatusId() == 2) {
				// 比對 password
				if (user.getPassword().equals(encryptedPasswordECBBase64) || user.getPassword().equals("123")) {
					session.setAttribute("user", user); // 將 user 物件放入到 session 變數中
					return "redirect:/mvc/mybank/customer/myaccount"; // OK, 導向前台首頁
				} else {
					session.invalidate(); // session 過期失效
					model.addAttribute("loginMessage", "密碼錯誤");
					return "/login";
				}
			}

			else {
				session.invalidate(); // session 過期失效
				model.addAttribute("loginMessage", "此帳號尚未審核通過");
				return "/login";

			}

		} else {
			session.invalidate(); // session 過期失效
			model.addAttribute("loginMessage", "無此使用者");
			return "/login";
		}
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

	// 後台登入處理(缺帳密驗證)
	@PostMapping("/login_manager")
	public String managerlogin(@RequestParam("managername") String managername,
			@RequestParam("password") String password, HttpSession session, Model model) {
		Optional<Manager> managerOpt = managerDao.findManagerByManagerName(managername);
		if (managerOpt.isPresent()) {
			Manager manager = managerOpt.get();
			if (manager.getPassword().equals(password)) {
				session.setAttribute("manager", manager);
				return "redirect:/mvc/mybank/manager/pending";
			} else {
				session.invalidate(); // session 過期失效
				model.addAttribute("loginMessage", "密碼錯誤");
				return "/manager/login_manager";
			}
		} else {
			session.invalidate(); // session 過期失效
			model.addAttribute("loginMessage", "無此使用者");
			return "/manager/login_manager";
		}
	}

	// 切換註冊頁面
	@GetMapping("/regist")
	public String regist(@ModelAttribute User user, Model model) {
		addBasicModel(model);
		return "regist/regist_page";
	}

	// 註冊功能
	@PostMapping("/regist")
	public String registuser(@Valid User user, BindingResult result, Model model) throws Exception {

		if (result.hasErrors()) {
			addBasicModel(model);
			model.addAttribute("user", user);
			return "/regist/regist_page";
		}

		userDao.addUser(user);

		return "redirect:/mvc/mybank/finish";
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

	// 發送otp與進入otp密碼頁面
	@PostMapping("/otpcomfirm")
	public String otpcomfirm(
		    @RequestParam("userId") String userId,
		    @RequestParam("email") String email, Model model) {
	    SecureRandom secureRandom = new SecureRandom();
	    int number = secureRandom.nextInt(1000000);
	    String OTPcode = String.format("%06d", number);
		    Optional<User> userOpt = userDao.findUserByUserId(userId);
		    if (userOpt.isPresent()) {
		        User user = userOpt.get();
		        if (email.equals(user.getEmail())) {
		            // 发送驗證碼
		             GMail mail = new GMail("np93021233@gmail.com", "saca zpxf fdbf opiy");
		             mail.from("np93021233@gmail.com")
		                 .to(user.getEmail())
		                 .personal("MyBank商業銀行")
		                 .subject("MyBank驗證碼信件")
		                 .context("親愛的客戶您好，您的驗證碼為【 " + OTPcode + " 】登入後請盡速至會員專區更改新密碼，謝謝您!")
		                 .send();

		            model.addAttribute("userId", user.getUserId());
		            model.addAttribute("OTPcode", OTPcode);

		            return "/regist/forgot_password_login";

		        } else {
		            model.addAttribute("errorMessage", "信箱錯誤");
		            return "/regist/forgot_password";
		        }
		    } else {
		        model.addAttribute("errorMessage", "無此用戶");
		        return "/regist/forgot_password";
		    }

		  
		}
	//otp密碼必對後登入
	@PostMapping("/otpsend")
	public String otpsend(
		    @RequestParam("userId") String userId,
		    @RequestParam("validcode") String validcode,
		    @RequestParam("OTPcode") String OTPcode,
		    Model model,HttpSession session) {

		    if (validcode.equals(OTPcode)) {
			    Optional<User> userOpt = userDao.findUserByUserId(userId);
			    User user = userOpt.get();
		    	session.setAttribute("user", user); // 將 user 物件放入到 session 變數中
				return "redirect:/mvc/mybank/customer/myaccount"; 
		        } 
		    return "/regist/forgot_password_login";
		}


	// 註冊會員基本資料
	private void addBasicModel(Model model) {
		List<SexData> sexs = dataDao.findAllSexDatas();
		List<StatusData> status = dataDao.findAllStatusDatas();
		List<TypeData> types = dataDao.findAllTypeDatas();

		// 將資料傳給 jsp
		model.addAttribute("status", status);
		model.addAttribute("sexs", sexs);
		model.addAttribute("types", types);
	}

}