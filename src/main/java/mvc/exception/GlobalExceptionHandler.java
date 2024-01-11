package mvc.exception;

import java.beans.PropertyEditorSupport;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jasper.JasperException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.servlet.resource.NoResourceFoundException;

// Controller 全局 AOP 
/**
 * @ControllerAdvice 是 Spring 框架中的一個非常有用的註解，它允許你通過一個集中的方式處理整個應用程序的異常、綁定數據以及在控制器（Controllers）上進行前置處理。
 * @ControllerAdvice 可以被視為針對 Spring 控制器（Controllers）的一種面向切面編程（AOP）機制。
 * 它提供了一種集中式的方式來處理跨多個控制器的橫切關注點（cross-cutting concerns），如異常處理、數據綁定和預設模型屬性。
 * 
 * 這個註解的主要功能包括：
 * 全局異常處理：通過 @ExceptionHandler 註解在 @ControllerAdvice 註解的類中處理異常。這意味著你可以在一個地方定義所有控制器的異常處理邏輯，而不需要在每個控制器中個別定義。
 * 數據綁定：可以使用 @InitBinder 註解來自定義請求參數的綁定和格式化規則。這在處理日期和時間格式時特別有用。
 * Model屬性設置：使用 @ModelAttribute 註解，你可以為所有控制器方法定義默認的模型屬性。這在需要在多個控制器之間共享數據時非常有用。
 * */
@ControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleExceptionDataIntegrityViolation(Exception ex, Model model) {
		model.addAttribute("errorMessage", "照片檔案太大，請重新上傳" + ex);
		return "error";
	}
	@ExceptionHandler(DuplicateKeyException.class)
	public String handleExceptionDuplicateKey(Exception ex, Model model) {
		model.addAttribute("errorMessage", "身份證字號重複，請重新確認" );
		return "error";
	}
    
	
    @ExceptionHandler(NullPointerException.class)
    public String handleExceptionNullPointer(Exception ex, Model model) {
        model.addAttribute("errorMessage", "您已登出，請重新登入" );
        model.addAttribute("droppath", "." );
        return "error";
    }
    @ExceptionHandler(JasperException.class)
    public String handleExceptionJasper(Exception ex, Model model) {
    	model.addAttribute("errorMessage", " JSP頁面編譯錯誤，請重新執行程式" + ex);
    	return "error";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
    	model.addAttribute("errorMessage", "其他錯誤:" + ex);
    	return "error";
    }

    /**
     * 當控制器收到含有日期的請求時（例如表單提交），Spring 會使用這個編輯器自動將字符串日期轉換為 Date 對象。
     * 就可以在控制器方法中直接使用 Date 類型的參數，而不需要自行解析日期字符串。
     * */
    //@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(dateFormat.parse(text));
                } catch (ParseException e) {
                    setValue(null);
                }
            }
            
            @Override
            public String getAsText() {
                return dateFormat.format((Date) getValue());
            }
        });
    }

    // 您可以添加更多的異常處理方法來處理不同類型的異常
}