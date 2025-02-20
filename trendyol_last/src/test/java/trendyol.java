import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.ArrayList;

public class trendyol {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver",
                "/Users/sumeyye/IdeaProjects/trendyol/src/main/resources/drivers/chromedriver" );
        // Start WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Go to Trendyol website
            driver.get("https://www.trendyol.com/");
            driver.manage().window().maximize(); // Maximize browser window

            // Wait for elements to load using WebDriverWait
            WebDriverWait wait = new WebDriverWait(driver, 10);

            WebElement popUpModel = driver.findElement(By.id("gender-popup-modal")); // Close pop-up
            popUpModel.click();
            WebElement accept = driver.findElement(By.id("onetrust-accept-btn-handler")); // Accept cookies
            accept.click();

            // Find the search area and wait for it to be clickable
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-testid='suggestion']")));

            // Type "kablosuz kulaklık" (wireless earphone)
            String searchText = "kablosuz kulaklık";
            searchBox.sendKeys(searchText);

            // Verify the text in the search box
            String enteredText = searchBox.getAttribute("value");
            assertTrue("The phrase \"kablosuz kulaklık\" was not written in the search box", enteredText.equals(searchText));

            // Press the enter key
            searchBox.sendKeys(Keys.RETURN);

            WebElement element =  driver.findElement(By.xpath("//*[@id=\"search-app\"]/div/div/div/div[2]/div[1]/div[1]/div"));
            // Get the text of the clicked element
            String elementText = element.getText();
            // Store the text in an object
            String myText = elementText;
            // Print the object to the screen
            System.out.println("Text of the clicked element:\n" + myText);

            // After the product page loads, get the product name
            WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"search-app\"]/div/div/div/div[2]/div[4]/div[1]/div/div[1]/div/div[2]/a[1]")));
            String name = productName.getText();
            System.out.println("Product Name: " + name);

            WebDriverWait wait3 = new WebDriverWait(driver, 40);
            productName.click();

            // Get the handles of the current tabs
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

            // Switch to the second tab
            driver.switchTo().window(tabs.get(1));
            WebElement acceptAddress = driver.findElement(By.xpath("//button[@class='onboarding-popover__default-renderer-primary-button']"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", acceptAddress);

            WebDriverWait wait4 = new WebDriverWait(driver, 10);  // Wait 10 seconds

            // Get the product price
            WebElement productPrice = wait3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"product-detail-app\"]/div/div[2]/div/div[2]/div[2]/div/div[1]/div[1]/div/div/div[3]/div/div/div/div[2]/span[2]")));
            String price = productPrice.getText();
            System.out.println("Product Price: " + price);

            // Extract the price from the product name
            String namePrice = name.replaceAll("[^\\d,]", ""); // Extract only numbers and commas from product name
            System.out.println("Extracted Price from Product Name: " + namePrice);

            // Remove 'TL' from the product price
            String productPriceValue = price.replaceAll("[^\\d,]", ""); // Remove 'TL' and other texts from the price

            // Check if the prices match
            if (namePrice.equals(productPriceValue)) {
                System.out.println("The product name and its price match!");
            } else {
                System.out.println("The product name and its price do not match!");
            }

            Thread.sleep(2000);

            WebElement addBasket = driver.findElement(By.xpath("//*[@class='add-to-basket-button-text']"));
            addBasket.click();

            Thread.sleep(2000);
            WebElement goBasket = driver.findElement(By.xpath("//*[@class='go-to-basket-product-text']"));
            goBasket.click();

            Thread.sleep(2000);

            Actions actions = new Actions(driver);
            actions.moveByOffset(10, 10).click().perform();
            Thread.sleep(2000);

            WebElement addbutton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='ty-numeric-counter-button']")));
            addbutton.click();

            Thread.sleep(2000);

            WebElement afteraddprice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='price-wrapper']")));
            String price2 = afteraddprice.getText();
            System.out.println("Price after adding to cart: " + price2);

            Thread.sleep(2000);

            WebElement totalprice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='pb-summary-total-price discount-active']")));
            String totalPriceText = totalprice.getText().replace("Toplam", "").trim(); // Remove the word "Toplam"
            System.out.println("Total price in cart: " + totalPriceText);

            if (price2.equals(totalPriceText)) {
                System.out.println("The price in cart and total price match!");
            } else {
                System.out.println("The price in cart and total price do not match!");
            }

            // Remove product from cart
            WebElement trash = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@aria-label='Ürünü sepetten çıkartma']")));
            trash.click();

            // Check if the item is removed from the cart
            boolean isRemoved;
            try {
                isRemoved = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@aria-label='Ürünü sepetten çıkartma']")));
            } catch (Exception e) {
                isRemoved = false; // If timeout occurs, assume it was not removed
            }

            // Print removal status
            if (isRemoved) {
                System.out.println("Removal successful: The product was removed from the cart.");
            } else {
                System.out.println("Removal failed: The product is still in the cart.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close browser
            driver.quit();
        }
    }
}
