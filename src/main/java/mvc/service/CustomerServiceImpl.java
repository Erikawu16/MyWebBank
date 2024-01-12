package mvc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mvc.bean.Account;
import mvc.bean.AccountDetail;
import mvc.bean.AccountRecord;
import mvc.bean.Currency;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private DataDao dataDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ManagerDao managerDao;

	@Override
	public List<Map<String, Object>> findAccountList(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return userDao.findUserAccountByUserId(user.getId());
	}

	@Override
	public User getUser(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return userDao.findUserByUserId(user.getUserId()).get();

	}

	@Override
	public List<Map<String, Object>> findAccountRecorder(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return userDao.findAccountRecorderByUserId(user.getId());

	}

	@Override
	public List<Map<String, Object>> findAccountInfoList(HttpSession session, Integer currencyId) {
		User user = (User) session.getAttribute("user");
		return userDao.findAccountRecorderByCurrencyId(user.getId(), currencyId);
	}

	@Override
	public String formattedDate() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return currentTime.format(formatter);
	}

	@Override
	public List<Currency> findCurrencyLisy() {
		return userDao.findAllCurrency();
	}

	@Override
	public Currency getcurrency(Integer currencyId) {
		return userDao.getCurrencyById(currencyId).get();
	}

	@Override
	public Account getAccount(HttpSession session, Integer currencyId) {
		User user = (User) session.getAttribute("user");
		return userDao.getUserAccountByCurrencyId(currencyId, user).get();
	}

	@Override
	public boolean isMoneyEnough(String moneyOutAmount, Integer moneyOutCurrencyId, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Float currentAmount = userDao.getUserAccountByCurrencyId(moneyOutCurrencyId, user).get().getAmount();
		if (Float.parseFloat(moneyOutAmount) <= currentAmount) {
			return true;
		}
		return false;
	}

	@Override
	public void addAccountDetail(String accountoutId, String accountinId, Integer moneyOutCurrencyId,
			String moneyOutAmount, Integer moneyInCurrencyId, String moneyInAmount, Float exchangeRate,
			HttpSession session) {
		AccountDetail accountDetail = new AccountDetail();
		User user = (User) session.getAttribute("user");
		accountDetail.setUserId(user.getId());
		///////////////////////////////////////////////////////////////
		// 轉出帳戶與金額
		accountDetail.setFrom_currencyId(moneyOutCurrencyId);
		accountDetail.setFrom_accountId(Integer.parseInt(accountoutId));
		accountDetail.setMoneyout(Float.parseFloat(moneyOutAmount) * (-1));
		///////////////////////////////////////////////////////////////
		// 轉入帳戶與金額
		accountDetail.setTo_currencyId(moneyInCurrencyId);
		accountDetail.setTo_accountId(Integer.parseInt(accountinId));
		accountDetail.setMoneyin(Float.parseFloat(moneyInAmount));
		// 匯率
		accountDetail.setRate(exchangeRate);
		userDao.addAccountDetail(accountDetail);
	}

	@Override
	public void addOutputAccountRecoder(String accountoutId, Integer moneyOutCurrencyId, String moneyOutAmount,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		Account accountOutamountTotal = userDao.getUserAccountByCurrencyId(moneyOutCurrencyId, user).get();
		userDao.updateAccountAmount(moneyOutCurrencyId,
				accountOutamountTotal.getAmount() - Float.parseFloat(moneyOutAmount), user.getId());

		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setUserId(user.getId());
		accountRecord.setCurrencyId(moneyOutCurrencyId);
		accountRecord.setAccountId(Integer.parseInt(accountoutId));
		accountRecord.setTransationmoney(Float.parseFloat(moneyOutAmount) * (-1));
		accountRecord.setCurrentAmount(accountOutamountTotal.getAmount() - Float.parseFloat(moneyOutAmount));
		userDao.addAccountRecorder(accountRecord);

	}

	@Override
	public void addInputAccountRecoder(String accountinId, Integer moneyInCurrencyId, String moneyInAmount,
			HttpSession session) {
		User user = (User) session.getAttribute("user");
		Account accountInamountTotal = userDao.getUserAccountByCurrencyId(moneyInCurrencyId, user).get();
		userDao.updateAccountAmount(moneyInCurrencyId,
				accountInamountTotal.getAmount() + Float.parseFloat(moneyInAmount), user.getId());

		AccountRecord accountRecord = new AccountRecord();
		accountRecord.setUserId(user.getId());
		accountRecord.setCurrencyId(moneyInCurrencyId);
		accountRecord.setAccountId(Integer.parseInt(accountinId));
		accountRecord.setTransationmoney(Float.parseFloat(moneyInAmount));
		accountRecord.setCurrentAmount(accountInamountTotal.getAmount() + Float.parseFloat(moneyInAmount));
		userDao.addAccountRecorder(accountRecord);

	}

	@Override
	public List<Map<String, Object>> findAccountDetail(HttpSession session) {
		User user = (User) session.getAttribute("user");

		return userDao.findAccountDetailByUserId(user.getId());
	}

	@Override
	public List<Map<String, Object>> findAccountDetail(String startDateStr, String endDateStr, HttpSession session) {
		LocalDate endDate = LocalDate.parse(endDateStr);
		LocalDate endDatePlusOneDay = endDate.plusDays(1);
		String endDatePlusOneDayStr = endDatePlusOneDay.toString();
		User user = (User) session.getAttribute("user");
		return userDao.findAccountDetailByTime(user.getId(), startDateStr, endDatePlusOneDayStr);
	}

	@Override
	public List<Currency> findUnregisteredAccounts(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return userDao.findUserUnregisteredForeignAccountsByUserId(user.getId());
	}

	@Override
	public boolean isUserOpt(HttpSession session) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userDao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			return true;

		}
		return false;
	}

	@Override
	public void addForeignAccount(Integer Foreignaccount, HttpSession session) {
		User user = (User) session.getAttribute("user");
		userDao.addUserForeignAccount(user, Foreignaccount);
		
	}
	
	

}
