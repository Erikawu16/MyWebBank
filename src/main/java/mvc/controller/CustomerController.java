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
import mvc.service.CustomerService;
import mvc.service.ManagerService;

@Controller
@RequestMapping("/mybank/customer")
public class CustomerController {

	@Autowired
	private UserDao userdao;
	@Autowired
	private DataDao dataDao;

	@Autowired
	private CustomerService customerService;

	// 我的帳戶首頁
	@GetMapping(value = { "/myaccount" })
	public String accountPage(Model model, HttpSession session) {
		model.addAttribute("account", customerService.findAccountList(session));
		model.addAttribute("user", customerService.getUser(session));
		return "customer/accountdetail";
	}

	// 我的帳戶詳細資料
	@GetMapping(value = { "/myaccount_infor" })
	public String accountinfor(Model model, HttpSession session) {
		// 取得使用者
		customerService.getUser(session);
		// 使用這所擁有的帳戶選單
		model.addAttribute("useraccount", customerService.findAccountList(session));
		// 全部帳戶交易資訊
		model.addAttribute("useraccountinfor", customerService.findAccountRecorder(session));
		return "customer/account_infor";
	}

	// 我的帳戶>>透過帳戶查詢交易紀錄
	@GetMapping(value = { "/searchaccount" })
	public String searchaccount(@RequestParam("currencyId") Integer currencyId, Model model, HttpSession session) {
		customerService.getUser(session);
		// 選單為使用這所擁有的帳戶
		model.addAttribute("useraccount", customerService.findAccountList(session));// 使用者所擁有之帳戶
		// 渲染特定帳戶交易
		// 如果列表無資料>>設定提示
		if ((customerService.findAccountInfoList(session, currencyId)).isEmpty()) {
			model.addAttribute("noDataMessage", "尚無交易紀錄");
		} else {
			// 渲染特定帳戶資料
			model.addAttribute("useraccountinfor", customerService.findAccountInfoList(session, currencyId));
		}

		return "customer/account_infor";
	}

	// 換匯頁面基本資料
	private void exchangeBasicModel(Model model, HttpSession session) {
		model.addAttribute("user", customerService.getUser(session));
		model.addAttribute("searchTime", customerService.formattedDate());// 時間
		model.addAttribute("useraccount", customerService.findAccountList(session));// 使用者所擁有之帳戶
		model.addAttribute("currency", customerService.findCurrencyLisy());// 匯率表
	}

	// 換匯首頁
	@GetMapping(value = { "/myexcahange" })
	public String excahangPage(Model model, HttpSession session) {
		exchangeBasicModel(model, session);
		return "customer/excahange";
	}
//匯率更新按鈕
	@GetMapping(value = { "/getNewCurrency" })
	public String getNewCurrency(Model model, HttpSession session) {
		exchangeBasicModel(model, session);
		customerService.updateCurrency();
		return "customer/excahange";
	}
	
	
	// 換匯確認頁面
	@PostMapping(value = { "/excahangecomfirm" })
	public String excahangcomfirm(@RequestParam("moneyOutSelect") Integer moneyOutSelect,
			@RequestParam("moneyInSelect") Integer moneyInSelect, @RequestParam("moneyin") Integer moneyinput,
			@RequestParam("moneyout") String moneyoutput, Model model, HttpSession session) {

		model.addAttribute("user", customerService.getUser(session));
		// 使用者所選擇的轉出幣別
		model.addAttribute("moneyOutSelect", customerService.getcurrency(moneyOutSelect));
		// 使用者所選擇的轉入幣別
		model.addAttribute("moneyInSelect", customerService.getcurrency(moneyInSelect));
		// 使用者所輸入的轉入帳戶
		model.addAttribute("accountin", customerService.getAccount(session, moneyInSelect));
		// 使用者轉入的金額
		model.addAttribute("moneyoutput", Float.parseFloat(moneyoutput));
		// 使用者所輸入的轉出金額
		model.addAttribute("moneyinput", moneyinput);
		// 使用者所輸入的轉出帳戶
		model.addAttribute("accountout", customerService.getAccount(session, moneyOutSelect));

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
		exchangeBasicModel(model, session);

		if (customerService.isMoneyEnough(moneyOutAmount, moneyOutCurrencyId, session)) {

			customerService.addAccountDetail(accountoutId, accountinId, moneyOutCurrencyId, moneyOutAmount,
					moneyInCurrencyId, moneyInAmount, exchangeRate, session);

			customerService.addOutputAccountRecoder(accountoutId, moneyOutCurrencyId, moneyOutAmount, session);
			customerService.addInputAccountRecoder(accountinId, moneyInCurrencyId, moneyInAmount, session);

			model.addAttribute("successMessage", "【換匯成功，請至交易紀錄查詢】");

		} else {
			model.addAttribute("errorMessage", "換匯失敗>>轉出帳戶餘額不足");
		}

		return "customer/excahange";
	}
	
	

	// 交易紀錄首頁
	@GetMapping(value = { "/myrecorder" })
	public String myrecoderPage(Model model, HttpSession session) {

		model.addAttribute("user", customerService.getUser(session));
		// 透過使用者id找到其交易紀錄並渲染到前端
		model.addAttribute("accountdetail", customerService.findAccountDetail(session));
		return "customer/recorder";

	}

	// 透過時間區間查詢交易紀錄
	@GetMapping(value = { "/searchrecorder" })
	public String searchrecorder(@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, Model model, HttpSession session) {
		model.addAttribute("user", customerService.getUser(session));

		List<Map<String, Object>> accountdetail = customerService.findAccountDetail(startDateStr, endDateStr, session);

		if (accountdetail.isEmpty()) {
			model.addAttribute("noDataMessage", "尚無交易紀錄");
		}
		model.addAttribute("accountdetail", accountdetail);
		return "customer/recorder";
	}

	// 會員資料首頁
	@GetMapping(value = { "/mydata" })
	public String data(HttpSession session, Model model) {
		if (customerService.isUserOpt(session)) {
			model.addAttribute("user", customerService.getUser(session));
			model.addAttribute("submitBtnName", "編輯會員資料");
			model.addAttribute("accountlist", customerService.findAccountList(session));
			// 匯率選單(只能顯示使用者尚未申請的)
			model.addAttribute("Unregisteredcurrency", customerService.findUnregisteredAccounts(session));
			return "customer/member_data";
		} else {
			model.addAttribute("errorMessage", "沒有此會員");
			return "customer/member_data";
		}
	}

	// 修改資會員資料頁面(變更密碼及信箱)
	@PostMapping("/data_change_page")
	public String datachangePage(HttpSession session, Model model) {
		if (customerService.isUserOpt(session)) {
			model.addAttribute("user", customerService.getUser(session));
			model.addAttribute("_method", "PUT");
			model.addAttribute("submitBtnName", "儲存會員資料");
			// 顯示使用者持有的帳戶
			model.addAttribute("accountlist", customerService.findAccountList(session));
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
		if (customerService.isUserOpt(session)) {
			// 將外幣帳戶加到該使用者的資料庫
			customerService.addForeignAccount(Foreignaccount, session);
			model.addAttribute("user", customerService.getUser(session));
			model.addAttribute("Foreignaccount", customerService.getcurrency(Foreignaccount));
			model.addAttribute("account", customerService.getAccount(session, Foreignaccount));
			return "customer/member_addaccount_finish";

		} else {
			model.addAttribute("errorMessage", "沒有此會員");
			return "customer/member_data";

		}

	}

}