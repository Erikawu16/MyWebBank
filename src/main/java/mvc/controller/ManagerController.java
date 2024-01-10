package mvc.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.Inflater;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.bean.Account;
import mvc.bean.Manager;
import mvc.bean.SexData;
import mvc.bean.StatusData;
import mvc.bean.TypeData;
import mvc.bean.User;
import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;

@Controller
@RequestMapping("/mybank/manager")
public class ManagerController {
	@Autowired
	private ManagerDao dao;

	@Autowired
	private UserDao userdao;

	@Autowired
	private DataDao dataDao;

	// header轉跳頁面區
	// pendingList(未審核會員資料) page
	@GetMapping(value = { "/pending" })
	public String pendingPage(HttpSession session, Model model) {
		Manager manager = (Manager) session.getAttribute("manager");

		List<User> users = dao.findUncheckUsers();
		model.addAttribute("users", users);
		if (manager.getLevelId().equals(1)) {
			model.addAttribute("pendingItemCount", dao.getPendingCount());
			return "manager/pendingList";
		}
		return "redirect:passuser";
	}

	// memberList(通過會員) page
	@GetMapping(value = { "/passuser" })
	public String passPage(HttpSession session, Model model) {
		Manager manager = (Manager) session.getAttribute("manager");
		model.addAttribute("accounts", dao.findPassUsers());

		if (manager.getLevelId().equals(1)) {
			model.addAttribute("pendingItemCount", dao.getPendingCount());
			return "manager/memberList";
		}
		return "manager/memberList";
	}

	// memberprofile
	@PostMapping(value = { "/{Id}" })
	public String profile(@PathVariable("Id") Integer Id, HttpSession session, Model model) {
		Manager manager = (Manager) session.getAttribute("manager");
		model.addAttribute("user", userdao.getUserById(Id).get());
		model.addAttribute("accountlist", userdao.findUserAccountByUserId(Id));

		if (manager.getLevelId().equals(1)) {
			model.addAttribute("pendingItemCount", dao.getPendingCount());
			return "manager/profile";
		}
		return "manager/profile";
	}

	// unapprovalList(未通過會員資料) page
	@GetMapping(value = { "/falseuser" })
	public String falsePage(Model model) {
		List<User> users = dao.findFalseUsers();
		model.addAttribute("users", users);
		model.addAttribute("pendingItemCount", dao.getPendingCount());
		return "manager/unapprovalList";

	}

	// 會員通過功能
	@PutMapping("/pass/{id}")
	public String passbtn(@PathVariable("id") Integer id, User user) {
		dao.updateUserStatusToPassById(id);
		dao.addUserAccount(id, user);
		return "redirect:/mvc/mybank/manager/pending";
	}

	// 會員未通過功能
	@PutMapping("/false/{id}")
	public String falsebtn(@PathVariable("id") Integer id, @RequestParam("falsereason") String falsereason) {
		dao.updateUserStatusToFalseById(id, falsereason);
		return "redirect:/mvc/mybank/manager/pending";
	}

	// 轉report(報告區) page
	@GetMapping(value = { "/report" })
	public String reportPage(Model model, HttpSession session) {
		Manager manager = (Manager) session.getAttribute("manager");
		model.addAttribute("menAmount", dao.getUserSexCount(1));
		model.addAttribute("womenAmount", dao.getUserSexCount(2));
		model.addAttribute("age1",dao.findUserAgeList().get(0));//18-29
		model.addAttribute("age2",dao.findUserAgeList().get(1));//30-39
		model.addAttribute("age3",dao.findUserAgeList().get(2));//40-49
		model.addAttribute("age4",dao.findUserAgeList().get(3));//50-59
		model.addAttribute("age5",dao.findUserAgeList().get(4));//60
		
		
		if (manager.getLevelId().equals(1)) {
			model.addAttribute("pendingItemCount", dao.getPendingCount());
			return "manager/reportpage";
		}
		return "manager/reportpage";
	}

}
