package mvc.service;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import mvc.bean.Currency;
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
import mvc.util.GMail;
import mvc.util.KeyUtil;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private DataDao dataDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ManagerDao managerDao;

	// 使用者登入
	@Override
	public LoginStatus isValidUser(String userId, String password, String code, String sessionCode) throws Exception {
		// 比對驗證碼
		if (!code.equals(sessionCode)) {
			return LoginStatus.CODE_ERROR;
		}
		Optional<User> userOpt = userDao.findUserByUserId(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			final String KEY = KeyUtil.getSecretKey();
			SecretKeySpec aesKeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			byte[] encryptedPasswordECB = KeyUtil.encryptWithAESKey(aesKeySpec, password);
			String encryptedPasswordECBBase64 = Base64.getEncoder().encodeToString(encryptedPasswordECB);

			System.out.println(user.getPassword());
			System.out.println(encryptedPasswordECBBase64);

			if (user.getStatusId() != 2)
				return LoginStatus.IN_REVIEW;

			if (user.getPassword().equals(encryptedPasswordECBBase64) || user.getPassword().equals("123")) {
				return LoginStatus.SUCCESS;
			} else {
				return LoginStatus.PASSWORD_ERROR;
			}
		}
		return LoginStatus.NO_USER;
	}

	// 取得驗證圖形
	@Override
	public void getCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
		Random random = new Random();
		String code1 = String.format("%c", (char) (random.nextInt(26) + 65));
		String code2 = String.valueOf(random.nextInt(10));
		String code3 = String.format("%c", (char) (random.nextInt(26) + 65));
		String code4 = String.valueOf(random.nextInt(10));
		String code = code1 + code2 + code3 + code4;
		session.setAttribute("code", code);
		// Java 2D 產生圖檔
		BufferedImage img = new BufferedImage(200, 80, BufferedImage.TYPE_INT_BGR);
		Graphics g = img.getGraphics();

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 200, 80);
		g.setColor(Color.WHITE);
		g.setFont(new Font("新細明體", Font.PLAIN, 50));
		g.drawString(code, 60, 55); // code, x, y
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

	public Currency getHomePageJPY() {
		return userDao.getCurrencyById(4).get();
	}
	public Currency getHomePageUSD() {
		return userDao.getCurrencyById(2).get();
	}
	public Currency getHomePageCNY() {
		return userDao.getCurrencyById(10).get();
	}

	// 後臺登入
	@Override
	public LoginStatus isValidManager(String managername, String password) {
		Optional<Manager> managerOpt = managerDao.findManagerByManagerName(managername);
		if (managerOpt.isPresent()) {
			Manager manager = managerOpt.get();
			if (manager.getPassword().equals(password)) {
				return LoginStatus.SUCCESS;
			} else {
				return LoginStatus.PASSWORD_ERROR;
			}
		} else {
			return LoginStatus.NO_USER;
		}
	}

	// 註冊會員基本資料
	public void addBasicModel(Model model) {
		List<SexData> sexs = dataDao.findAllSexDatas();
		List<StatusData> status = dataDao.findAllStatusDatas();
		List<TypeData> types = dataDao.findAllTypeDatas();
		model.addAttribute("status", status);
		model.addAttribute("sexs", sexs);
		model.addAttribute("types", types);
	}

	// 註冊資料是否完整
	public boolean isRegistDataPass(@Valid User user, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			addBasicModel(model);
			model.addAttribute("user", user);
			return false;
		}
		userDao.addUser(user);
		return true;
	}

	// 檢查信箱是否正確
	@Override
	public UserDataCheck isEmailValid(String userId, String email) {
		Optional<User> userOpt = userDao.findUserByUserId(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (email.equals(user.getEmail())) {
				return UserDataCheck.SUCCESS;
			}
			return UserDataCheck.EMAIL_ERROR;
		}
		return UserDataCheck.NO_USER;
	}

	// 發送OTP驗證碼
	public void sendOTP(String userId) {
		User user = userDao.findUserByUserId(userId).get();
		SecureRandom secureRandom = new SecureRandom();
		int number = secureRandom.nextInt(1000000);
		String OTPcode = String.format("%06d", number);
		GMail mail = new GMail("np93021233@gmail.com", "saca zpxf fdbf opiy");
		mail.from("np93021233@gmail.com").to(user.getEmail()).personal("MyBank商業銀行").subject("MyBank驗證碼信件")
				.context("親愛的客戶您好，您的驗證碼為【 " + OTPcode + " 】登入後請盡速至會員專區更改新密碼，謝謝您!").send();

	}
    //利用OTP登入會員
	public boolean isOTPValidUser(String validcode, String OTPcode) {
		if (validcode.equals(OTPcode)) {
			return true;
			}
		return false;
	}

}
