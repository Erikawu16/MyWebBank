package mvc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import mvc.bean.Account;
import mvc.bean.Currency;
import mvc.bean.User;

public interface CustomerService {

	User getUser(HttpSession session);

	List<Map<String, Object>> findAccountList(HttpSession session);

	List<Map<String, Object>> findAccountRecorder(HttpSession session);

	List<Map<String, Object>> findAccountInfoList(HttpSession session, Integer currencyId);

	String formattedDate();

	List<Currency> findCurrencyLisy();

	Currency getcurrency(Integer currencyId);

	Account getAccount(HttpSession session, Integer currencyId);

	boolean isMoneyEnough(String moneyOutAmount, Integer moneyOutCurrencyId, HttpSession session);

	void addAccountDetail(String accountoutId, String accountinId, Integer moneyOutCurrencyId, String moneyOutAmount,
			Integer moneyInCurrencyId, String moneyInAmount, Float exchangeRate, HttpSession session);

	void addOutputAccountRecoder(String accountoutId, Integer moneyOutCurrencyId, String moneyOutAmount,
			HttpSession session);

	void addInputAccountRecoder(String accountinId, Integer moneyInCurrencyId, String moneyInAmount,
			HttpSession session);

	List<Map<String, Object>> findAccountDetail(HttpSession session);

	List<Map<String, Object>> findAccountDetail(String startDateStr, String endDateStr, HttpSession session);

	List<Currency> findUnregisteredAccounts(HttpSession session);

	boolean isUserOpt(HttpSession session);
	void addForeignAccount(Integer Foreignaccount, HttpSession session);

	
	
	
}
