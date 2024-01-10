package mvc.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.bean.Account;
import mvc.bean.AccountDetail;
import mvc.bean.AccountRecord;
import mvc.bean.Currency;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;

@Controller
@RequestMapping("/mybank/customer")
public class CustomerController {

	@Autowired
	private UserDao userdao;
	@Autowired
	private DataDao dataDao;

	// 我的帳戶首頁
	@GetMapping(value = { "/myaccount" })
	public String accountPage(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			List<Map<String, Object>> accountlist = userdao.findUserAccountByUserId(user.getId());
			System.out.println(accountlist);
			model.addAttribute("account", accountlist);
			model.addAttribute("user", user);
			return "customer/accountdetail";
		}
		return "customer/accountdetail";
	}

	// 我的帳戶詳細資料
	@GetMapping(value = { "/myaccount_infor" })
	public String accountinfor(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		// 選單為使用這所擁有的帳戶
		model.addAttribute("useraccount", userdao.findUserAccountByUserId(user.getId()));
		// 渲染全部帳戶交易
		List<Map<String, Object>> a = userdao.findAccountRecorderByUserId(user.getId());
		model.addAttribute("useraccountinfor", userdao.findAccountRecorderByUserId(user.getId()));
		System.out.println(a);
		return "customer/account_infor";
	}

	// 我的帳戶>>透過帳戶查詢交易紀錄
	@GetMapping(value = { "/searchaccount" })
	public String searchaccount(@RequestParam("currencyId") Integer currencyId, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");

		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());

		// 選單為使用這所擁有的帳戶
		model.addAttribute("useraccount", userdao.findUserAccountByUserId(user.getId()));// 使用者所擁有之帳戶
		// 渲染特定帳戶交易
		List<Map<String, Object>> accountInfoList = userdao.findAccountRecorderByCurrencyId(user.getId(), currencyId);

		// 如果列表为空，设置消息提示
		if (accountInfoList.isEmpty()) {
			model.addAttribute("noDataMessage", "尚無交易紀錄");
		} else {
			// 渲染特定帳戶資料
			model.addAttribute("useraccountinfor", accountInfoList);
		}

		return "customer/account_infor";
	}

	// 換匯頁面基本資料
	private void exchangeBasicModel(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		model.addAttribute("user", user);
		// 取得當前時間並渲染到前端
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = currentTime.format(formatter);
		// 顯示使用者所擁有之帳戶(需要擁有帳戶才能開始換匯)
		List<Map<String, Object>> useraccount = userdao.findUserAccountByUserId(user.getId());
		// 下方匯率表資料
		dataDao.updateCurrency();
		List<Currency> currency = userdao.findAllCurrency();
		
		model.addAttribute("searchTime", formattedDate);// 時間
		model.addAttribute("useraccount", useraccount);// 使用者所擁有之帳戶
		model.addAttribute("currency", currency);// 匯率表
	}

	// 換匯首頁
	@GetMapping(value = { "/myexcahange" })
	public String excahangPage(Model model, HttpSession session) {
		exchangeBasicModel(model, session);
		return "customer/excahange";
	}

	// 換匯確認頁面
	@PostMapping(value = { "/excahangecomfirm" })
	public String excahangcomfirm(@RequestParam("moneyOutSelect") Integer moneyOutSelect,
			@RequestParam("moneyInSelect") Integer moneyInSelect, @RequestParam("moneyin") Integer moneyinput,
			@RequestParam("moneyout") String moneyoutput, Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			model.addAttribute("user", user);

			// 使用者所選擇的轉出幣別
			model.addAttribute("moneyOutSelect", userdao.getCurrencyById(moneyOutSelect).get());
			// 使用者所選擇的轉入幣別
			model.addAttribute("moneyInSelect", userdao.getCurrencyById(moneyInSelect).get());
			// 使用者所輸入的轉入帳戶
			model.addAttribute("accountin", userdao.getUserAccountByCurrencyId(moneyInSelect, user).get());
			// 使用者轉入的金額
			model.addAttribute("moneyoutput", Float.parseFloat(moneyoutput));

			// 使用者所輸入的轉出金額
			model.addAttribute("moneyinput", moneyinput);
			// 使用者所輸入的轉出帳戶
			model.addAttribute("accountout", userdao.getUserAccountByCurrencyId(moneyOutSelect, user).get());

			return "customer/excahange_confirm";
		}
		return "customer/excahange_confirm";
	}

	// 確認換匯功能
	@PostMapping(value = { "/excahange_finish" })
	public String excahang(@RequestParam("accountoutId") String accountoutId,
			@RequestParam("accountinId") String accountinId,
			@RequestParam("moneyOutCurrencyId") Integer moneyOutCurrencyId,
			@RequestParam("moneyOutAmount") String moneyOutAmount,
			@RequestParam("moneyInCurrencyId") Integer moneyInCurrencyId,
			@RequestParam("moneyInAmount") String moneyInAmount, @RequestParam("exchangeRate") Float exchangeRate,
			Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		exchangeBasicModel(model, session);
		Float currentAmount=userdao.getUserAccountByCurrencyId(moneyOutCurrencyId,user).get().getAmount();

		if (Float.parseFloat(moneyOutAmount)<=currentAmount) {

			// 新增詳細交易訊息
			AccountDetail accountDetail = new AccountDetail();

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
			int rowcount = userdao.addAccountDetail(accountDetail);

			// 取得的帳戶
			Account accountOutamountTotal = userdao.getUserAccountByCurrencyId(moneyOutCurrencyId, user).get();
			Account accountInamountTotal = userdao.getUserAccountByCurrencyId(moneyInCurrencyId, user).get();
			// 更新帳戶餘額
			userdao.updateAccountAmount(moneyOutCurrencyId,
					accountOutamountTotal.getAmount() - Float.parseFloat(moneyOutAmount), user.getId());
			userdao.updateAccountAmount(moneyInCurrencyId,
					accountInamountTotal.getAmount() + Float.parseFloat(moneyInAmount), user.getId());

			// 更新個別帳戶資訊
			AccountRecord accountRecord1 = new AccountRecord();
			accountRecord1.setUserId(user.getId());
			accountRecord1.setCurrencyId(moneyOutCurrencyId);
			accountRecord1.setAccountId(Integer.parseInt(accountoutId));
			accountRecord1.setTransationmoney(Float.parseFloat(moneyOutAmount));
			accountRecord1.setCurrentAmount(accountOutamountTotal.getAmount() - Float.parseFloat(moneyOutAmount));

			AccountRecord accountRecord2 = new AccountRecord();
			accountRecord2.setUserId(user.getId());
			accountRecord2.setCurrencyId(moneyInCurrencyId);
			accountRecord2.setAccountId(Integer.parseInt(accountinId));
			accountRecord2.setTransationmoney(Float.parseFloat(moneyInAmount) * (-1));
			accountRecord2.setCurrentAmount(accountInamountTotal.getAmount() + Float.parseFloat(moneyInAmount));
			userdao.addAccountRecorder(accountRecord1);
			userdao.addAccountRecorder(accountRecord2);
			
			model.addAttribute("successMessage", "【換匯成功，請至交易紀錄查詢】");
			
		} else {
			model.addAttribute("errorMessage", "換匯失敗>>轉出帳戶餘額不足");
		}

		return "customer/excahange";
	}


	
	
	
	
	// 交易紀錄首頁
	@GetMapping(value = { "/myrecorder" })
	public String myrecoderPage(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			model.addAttribute("user", user);
			// 透過使用者id找到其交易紀錄並渲染到前端
			model.addAttribute("accountdetail", userdao.findAccountDetailByUserId(user.getId()));
			System.out.println(userdao.findAccountDetailByUserId(user.getId()));
			return "customer/recorder";
		}

		return "customer/recorder";
	}

	// 透過時間區間查詢交易紀錄
	@GetMapping(value = { "/searchrecorder" })
	public String searchrecorder(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model, HttpSession session) {

		// 將字串轉為時間格式
		LocalDate endDate = LocalDate.parse(endDateStr);
		// 將結束時間加一天
		LocalDate endDatePlusOneDay = endDate.plusDays(1);
		// 再轉回字串
		String endDatePlusOneDayStr = endDatePlusOneDay.toString();

		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			model.addAttribute("user", user);
			// 透過使用者id找到其交易紀錄並渲染到前端

			List<Map<String, Object>> accountdetail = userdao.findAccountDetailByTime(user.getId(), startDateStr,
					endDatePlusOneDayStr);
			// 如果列表为空，设置消息提示
			if (accountdetail.isEmpty()) {
				model.addAttribute("noDataMessage", "尚無交易紀錄");
			} else {
				// 渲染特定帳戶資料
				model.addAttribute("accountdetail", accountdetail);
				return "customer/recorder";
			}

		}
		return "customer/recorder";

	}

	// 會員信箱
	@GetMapping(value = { "/mybox" })
	public String memberboxPage() {
		return "customer/member_box";
	}

	// 會員資料首頁
	@GetMapping(value = { "/mydata" })
	public String data(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			// 渲染到會員基本資料
			model.addAttribute("user", user);
			model.addAttribute("submitBtnName", "編輯會員資料");

			// 顯示使用者持有的帳戶
			List<Map<String, Object>> accountList = userdao.findUserAccountByUserId(user.getId());
			model.addAttribute("accountlist", accountList);

			// 匯率選單(只能顯示使用者尚未申請的)
			List<Currency> Unregisteredcurrency = userdao.findUserUnregisteredForeignAccountsByUserId(user.getId());
			model.addAttribute("Unregisteredcurrency", Unregisteredcurrency);
			return "customer/member_data";
		} else {
			model.addAttribute("errorMessage", "沒有此會員");
			return "customer/member_data";
		}
	}

	// 修改資會員資料頁面(變更密碼及信箱)
	@PostMapping("/data_change_page")
	public String datachangePage(HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			model.addAttribute("user", user);
			model.addAttribute("_method", "PUT");
			model.addAttribute("submitBtnName", "儲存會員資料");
			// 顯示使用者持有的帳戶
			List<Map<String, Object>> accountList = userdao.findUserAccountByUserId(user.getId());
			model.addAttribute("accountlist", accountList);

			userOpt.get();
			return "customer/member_data_change";
		} else {
			model.addAttribute("errorMessage", "沒有此會員");
			return "customer/member_data";
		}
	}

	// 修改資料功能
	@PutMapping("/data_change")
	public String datachange(@RequestParam("newEmail") String newEmail, @RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPasswords") List<String> newPasswords, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		if (!user.getPassword().equals(oldPassword)) {
			model.addAttribute("submitBtnName", "儲存會員資料");
			model.addAttribute("errorMessage", "原密碼錯誤");
			return "customer/member_data_change";
		}
		if (!newPasswords.get(0).equals(newPasswords.get(1))) {
			model.addAttribute("submitBtnName", "儲存會員資料");
			model.addAttribute("errorMessage", "二次新密碼不一致");
			return "customer/member_data_change";
		}
		// 進行密碼變更
		userdao.updateUserById(user.getUserId(), newPasswords.get(0), newEmail);
		model.addAttribute("submitBtnName", "編輯會員資料");
		model.addAttribute("sussesMessage", "修改成功!下次請使用新密碼登入");

		return "customer/member_data";
	}

	// 新增外幣帳戶功能
	@PostMapping("/addaccount")
	public String addAccount(@RequestParam("Foreignaccount") Integer Foreignaccount, HttpSession session, Model model) {
		User user = (User) session.getAttribute("user");
		Optional<User> userOpt = userdao.findUserByUserId(user.getUserId());
		if (userOpt.isPresent()) {
			userOpt.get();
			// 將外幣帳戶加到該使用者的資料庫
			userdao.addUserForeignAccount(user, Foreignaccount);

			// 取得該外幣帳戶資料
			Account account = userdao.getUserAccountByCurrencyId(Foreignaccount, user).get();

			model.addAttribute("user", user);
			model.addAttribute("Foreignaccount", userdao.getCurrencyById(Foreignaccount).get());
			model.addAttribute("account", account);

			return "customer/member_addaccount_finish";

		} else {
			model.addAttribute("errorMessage", "沒有此會員");
			return "customer/member_data";

		}

	}

}

//個人資料專區功能end
