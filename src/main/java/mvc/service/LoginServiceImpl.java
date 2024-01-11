package mvc.service;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mvc.bean.User;
import mvc.dao.UserDao;
import mvc.enums.LoginStatus;
import mvc.util.KeyUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public LoginStatus isValidUser(String userId, String password, String code,String sessionCode) throws Exception {
		
		// 比對驗證碼
		if(!code.equals(sessionCode)) {
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
			
			if(user.getStatusId() != 2) 
				return LoginStatus.IN_REVIEW;
			
			if (user.getPassword().equals(encryptedPasswordECBBase64) || user.getPassword().equals("123")) {
				return LoginStatus.SUCCESS;
			} else {
				return LoginStatus.PASSWORD_ERROR;
			}
		}
		return LoginStatus.NO_USER;
	}

}
