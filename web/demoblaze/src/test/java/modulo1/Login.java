package modulo1;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import modulo1.EscribirArchivoExcel;
import modulo1.LeerArchivoExcel;

public class Login {
	private static WebDriver driver;
	private static EscribirArchivoExcel escribirArchivo;
	private static LeerArchivoExcel leerArchivo;
	private By Login = By.xpath("/html/body/nav/div[1]/ul/li[5]/a");
	private By User = By.xpath("/html/body/div[3]/div/div/div[2]/form/div[1]/input");
	private By password = By.xpath("/html/body/div[3]/div/div/div[2]/form/div[2]/input");
	private By botonIngresar = By.xpath("/html/body/div[3]/div/div/div[3]/button[2]");
	private By botonSalir = By.xpath("/html/body/nav/div[1]/ul/li[6]/a");
	String mensaje = driver.findElement(By.xpath("/html/body/nav/div[1]/ul/li[7]/a")).getText();


	@BeforeClass
  public void beforeClass() throws IOException {
	System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
	driver = new ChromeDriver();
	escribirArchivo = new EscribirArchivoExcel();
	leerArchivo = new LeerArchivoExcel();
	driver.manage().window().maximize();

  }
  
  @AfterClass
  public void afterClass() {
	  //driver.quit();
  }
  
  @Test(dataProvider = "DataCasosPrueba")
  public void EjecutarAprobarSolicitud(String searchTextUser, String searchTextPassword, String nameScreenshot ) throws IOException, InterruptedException {
	  
	  driver.get("https://www.demoblaze.com/index.html");
	  
	  //Ingresando nuevas credenciales
	  Thread.sleep(2000);
	  
	  //Clic en el botón registrarse
	  driver.findElement(Login).click();	
	  
	  //USER
	  Thread.sleep(2000);
	  driver.findElement(User).click();	
	  driver.findElement(User).sendKeys(searchTextUser);
	  	
	  //PASWOORD
	  Thread.sleep(2000);
	  driver.findElement(password).click();
	  driver.findElement(password).sendKeys(searchTextPassword);

	  //Iniciando sesion
	  driver.findElement(botonIngresar).click();
			
	  //Capturando la evidencia
	  Thread.sleep(7000);
	  File rutaArchivo= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  FileUtils.copyFile(rutaArchivo, new File("D:\\NuevaCredenciales\\Evidencia\\" + nameScreenshot + ".png"));
	  
	  //cerrando sesion
	  driver.findElement(botonSalir).click();

	  //Capturando la evidencia
	  Thread.sleep(7000);
	  File rutaArchivo1= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  FileUtils.copyFile(rutaArchivo1, new File("D:\\NuevaCredenciales\\Evidencia\\" + nameScreenshot + "cerrandosesion" + ".png"));
	  
assertEquals("Welcome " + User, mensaje);
	
	  }

  @DataProvider(name = "DataCasosPrueba")
  public Object[][] getData() throws IOException {
	  	String filepath = "D:\\NuevaCredenciales\\Registracredenciales.xlsx";
		
	  	String sheetName2 = "Usuarios";
	  	
		FileInputStream inputStream = new FileInputStream(filepath);
		
		XSSFWorkbook newWorkbook = new  XSSFWorkbook(inputStream);
		
		XSSFSheet newSheet = newWorkbook.getSheet(sheetName2);
		
		int rowCount  = newSheet.getLastRowNum() + newSheet.getFirstRowNum();
	
	  Object [][]data =  new Object[rowCount][3];
	  
	  for (int i=0; i<rowCount;i++) {
		  
		  data[i][0]= leerArchivo.getCellValue(filepath, sheetName2, (i+1), 0);
		  data[i][1]= leerArchivo.getCellValue(filepath, sheetName2, (i+1), 1);
		  data[i][1]= leerArchivo.getCellValue(filepath, sheetName2, (i+1), 2);
	  }
	  return data;
  }
}
