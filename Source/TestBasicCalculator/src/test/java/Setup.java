import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Setup {
    public WebDriver GetDriver(String driver) {
        switch (driver) {
            case "Chrome": return GetChromeDriver();
            case "Firefox": return GetFireFoxDriver();
            case "Edge": return GetEdgeDriver();
            default: return GetChromeDriver();

        }
    };
    private WebDriver GetChromeDriver() {
        WebDriver _webDriver;
        System.setProperty("webdriver.chrome.driver", "src/test/driver/chromedriver.exe");
        _webDriver = new ChromeDriver();
        return _webDriver;
    }
    private WebDriver GetFireFoxDriver() {
        WebDriver _webDriver;
        System.setProperty("webdriver.gecko.driver", "src/test/driver/geckodriver.exe");
        _webDriver = new FirefoxDriver();
        return _webDriver;
    }
    private WebDriver GetEdgeDriver() {
        WebDriver _webDriver;
        System.setProperty("webdriver.edge.driver", "src/test/driver/msedgedriver.exe");
        _webDriver = new EdgeDriver();
        return _webDriver;
    }
}