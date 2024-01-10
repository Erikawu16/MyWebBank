package mvc.bean;

import java.sql.Date;
import java.util.List;

import lombok.Data;

//換匯詳細資訊
@Data
public class AccountDetail {
	int AccountDetailId;
	
	User user;
	Integer userId; 
	
	Currency fromcurrency;
	Integer  from_currencyId;
	
	Account fromaccount;
	Integer from_accountId;
	
	Float moneyout;//轉出金額
	//Float moneyout_amount;//轉出帳戶餘額
//---------------------------------------//
	Integer  to_currencyId;
	Currency tocurrency;
	
	Integer to_accountId;
	Account toaccount;
	
	Float moneyin;//轉入金額
	//Float moneyin_amount;//轉入帳戶餘額
//---------------------------------------//	
	
	
	Float rate;//即期匯率//Float bankin;//銀行買入匯率
	Date transaction_time;//交易日期
	
	
	
}
