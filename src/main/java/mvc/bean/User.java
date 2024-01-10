package mvc.bean;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.DecimalMax;


import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import lombok.Data;
@Data
// 對應 spring form 的表單資訊
public class User {
	private Integer id; // 序號

	@NotEmpty(message = "{user.username.noetmpty}")
	@Size(min = 3, max = 10, message = "{user.username.size}")
	@Pattern(regexp = "^[\\u4e00-\\u9fa5]{3,10}$", message = "{user.username.chinese}")
	private String username; // 姓名
	
	
	@NotEmpty(message = "{user.userId.notempty}")
	@Size(min=10, max = 10, message = "{user.userId.size}")
	private String userId ; // 身分證字號
	
	@NotEmpty(message = "{user.password.notempty}")
	@Size(min=10,max = 16, message = "{user.password.size}")
	private String password ; 
	
	
    @NotBlank(message = "{user.email.notblank}")
    @Email(message =  "{user.email.Email}")
	private String email ; // 信箱
	
	
	@NotNull(message = "{user.birth.notnull}")
	@Past(message = "{user.birth.Past}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date birth; // 生日

	
	private Date registDate; // 申請日期

	
	private Integer statusId;//狀態ID預設1
	private StatusData  status; // 狀態預設1(uncheck)

	@NotNull(message = "{user.sexId.notnull}")
	private Integer sexId; // 性別Id(給表單用)
	private SexData sex; // 性別(給list呈現用)

	@NotNull( message = "{user.typeId.notnull}")
	private Integer typeId; // 身分Id(給表單用)【稅務身分】
	private List<TypeData> type; // 身分(給list呈現用)
	
	@NotNull
	private String imgContent;
	// ---------------------------------------------------------

	private String falsereason;
	

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}