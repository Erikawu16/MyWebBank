package mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mvc.dao.DataDao;
import mvc.dao.ManagerDao;
import mvc.dao.UserDao;


@Service
public class CustomerServiceImpl implements CustomerService{
	@Autowired
	private DataDao dataDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ManagerDao managerDao;
}
