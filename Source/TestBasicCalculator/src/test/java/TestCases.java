
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class TestCases {

    private WebDriver _webDriver = null;

    @BeforeTest
    void Setup() {
        _webDriver = new Setup().GetDriver("Firefox");
        _webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(20));
        _webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        _webDriver.navigate().to("https://testsheepnz.github.io/BasicCalculator.html");
    }

    @DataProvider(name = "DataTable")
    public Object[][] getDataAsObject()  {
        String pathToFile = "src/test/resources/Data.csv";
        List<Object[]> tList = new ArrayList<Object[]>();
        String record;
        BufferedReader file;
        try {
            file = new BufferedReader(new InputStreamReader(new FileInputStream(pathToFile),"UTF-8"));
            file.readLine();
            while ((record=file.readLine()) != null) {
                String field[] = record.split(",");
                tList.add(field);
            }
            file.close();
            Object[][] result = new Object[tList.size()][];
            for (int i = 0; i < tList.size(); i++) {
                result[i] = tList.get(i);
            }
            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test(dataProvider = "DataTable")
     public void TestCase (String ID, String Build, String FirstNum, String SecondNum,String Operation, String Result, String IntResul, String Error) {
        System.out.println("Test case ID: " + ID);
        _webDriver.manage().window().maximize();
        if (!Objects.equals(Operation, "Concatenate")) {
            Boolean isSelected = _webDriver.findElement(By.id("integerSelect")).isSelected();
            if (isSelected == true) {
                JavascriptExecutor js_1 = (JavascriptExecutor) _webDriver;
                js_1.executeScript("arguments[0].click();", _webDriver.findElement(By.id("integerSelect")));
            }
        }

        _webDriver.findElement(By.id("number1Field")).clear();
        _webDriver.findElement(By.id("number2Field")).clear();
        _webDriver.findElement(By.id("clearButton")).click();

        // Choose build number
        new Select(_webDriver.findElement(By.id("selectBuild"))).selectByValue(Build);
        _webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Input number 1

        _webDriver.findElement(By.id("number1Field")).sendKeys(FirstNum);
        new WebDriverWait(_webDriver, Duration.ofMillis(5000));

        // Input number 2

        _webDriver.findElement(By.id("number2Field")).sendKeys(SecondNum);
        new WebDriverWait(_webDriver, Duration.ofMillis(5000));

        // Choose operation
        Select op = new Select(_webDriver.findElement(By.id("selectOperationDropdown")));
        op.selectByVisibleText(Operation);
        new WebDriverWait(_webDriver, Duration.ofMillis(5000));

        // Click Calculate button
        _webDriver.findElement(By.id("calculateButton")).click();


        if (Objects.equals(Error, "FALSE")) {
            WebDriverWait wait = new WebDriverWait(_webDriver, Duration.ofMillis(5000));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadEquipment")));

            String actualRs = _webDriver.findElement(By.id("numberAnswerField")).getAttribute("value");
            Assert.assertEquals(actualRs, Result);

            // Click 'intSelection'
            JavascriptExecutor js = (JavascriptExecutor) _webDriver;
            js.executeScript("arguments[0].click();", _webDriver.findElement(By.id("integerSelect")));
            _webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            // Checkpoint
            if (!Objects.equals(Operation, "Concatenate")) {
                String intActualRs = _webDriver.findElement(By.id("numberAnswerField")).getAttribute("value");
                Assert.assertEquals(intActualRs, IntResul);
            }

        } else {
            String actualErr = _webDriver.findElement(By.id("errorMsgField")).getText();
            Assert.assertEquals(actualErr, Error);
            if (Objects.equals(Error, "Divide by zero error!")) {
                // Divide by 0 can make the page stuck
                _webDriver.navigate().refresh();
                _webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            }
            //
        }

        // Click Clear button
        _webDriver.findElement(By.id("clearButton")).click();
        _webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));


    }

    @AfterTest
    void tearDown() {
        System.out.println("Tear Down");
        _webDriver.quit();
    }
}
