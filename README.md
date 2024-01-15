專題網址: http://52.64.144.115:8080//MyWebBank/
【系統架構】

以JAVA SpringMVC為基礎，透過controller進行路徑導向，Service層進行商業邏輯判斷，Repository向資料庫進行增加刪改查的動作， 再將結果返回至controller，並將資料渲染到JSP頁面，透過前端(HTML/CSS/JS/Bootstrap/GoogleChart...等技術)進行資料美化後返回給用戶端。

另外使用到Scheduler技術，定時向外部資源進行爬蟲後更新資料庫內容，凸顯系統與外部資料的整合能力，以及利用AES加密技術，讓機敏資料在傳輸時可以多一層保護。

此專題還利用JavaMail API 結合OTP隨機驗證碼，撰寫忘記密碼功能，雙驗證讓用戶使用上多一層保障。

再利用AOP技術將全域的Exception進行追蹤，以提升系統的穩定性和維護性，最後將整體專案利用AWS部屬到雲端。


【專題特色】

1.著重於用戶友好的界面>>採用RWD響應式網頁設計

2.登入時隨機驗證碼>>增強用戶登錄時的安全性
  OTP 驗證碼技術>>有效避免未經授權的訪問和欺詐行為
  
3.ASE密碼加密>>確保敏感信息在傳輸與儲存過程中受到保護以提高用戶的資料安全

4.管理員分級功能>>根據不同的管理員等級，提供不同的訪問權限>>確保敏感信息和功能只能被授權的人員訪問

5.即時匯率爬蟲>>提供最新及準確的匯率資訊>>提高交易效率

【專題動機】

1 金融科技日新月異：當前，金融業務在網絡平台上不斷演進。建立一個現代化的網路銀行系統已成為了金融行業的一個重要趨勢。

2 便利性與安全性的平衡：在人人離不開數位金融的時代，我深知安全性與便利性之間的挑戰。希望透過這個專題，去學習如何提高系統安全性，以滿足用戶對便利性和安全性的雙重需求。

3 商科背景：作為商科畢業生，我深知金融業務的重要性，透過技術創新與發展，我希望能夠在金融領域提供更安全、更方便的服務。尤其是在資訊安全、交易效率和管理準確性方面，我也將這些需求融入到本次專題，致力提供一個安全便利與高效的系統。





