import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Paths;

/****************************************/
//Historia de Usuario:
//Como postulante interesado en estudiar en la UCB,
//quiero ingresar a la sección de Becas desde la página principal,
//para conocer si existen beneficios económicos disponibles.
//
//Prueba de Aceptacion / Caso de Prueba TC:200:
//Verificar que un postulante pueda acceder a la sección de Becas desde la página principal de la UCB.
//
//Titulo: TC:200 - Acceder a la sección de Becas desde la página principal
//
//PASO 1. Ingresar a la pagina de la UCB https://lpz.ucb.edu.bo/
//PASO 2. Buscar y hacer click en el enlace Becas del menu principal
//PASO 3. Esperar que cargue la pagina de Becas
//
//Resultado Esperado:
//La página debe mostrar contenido relacionado con Becas y la URL o el contenido debe contener la palabra "beca".
/****************************************/

//Para ejecutar en la linea de comando:
//mvn clean test -Dtest=BecasUCBTest

public class BecasUCBTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeTest
    public void setUp() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(700)
        );

        // Configurar grabación de video al escritorio
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get(desktopPath)));

        page = context.newPage();
    }

    @Test
    public void ingresarASeccionBecasTest() {

        /********** Preparacion de la prueba **********/

        //PASO 1. Ingresar a la pagina de la UCB https://lpz.ucb.edu.bo/
        String urlUCB = "https://lpz.ucb.edu.bo/";

        page.navigate(urlUCB);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("Se ingreso correctamente a la pagina principal de la UCB.");
        System.out.println("Titulo de la pagina: " + page.title());

        /*********** Logica de la prueba ***********/

        //PASO 2. Buscar y hacer click en el enlace Becas del menu principal
        Locator enlaceBecas = page.locator("a:has-text('Becas')").first();

        Assert.assertTrue(
                enlaceBecas.isVisible(),
                "El enlace Becas debe estar visible en la pagina principal."
        );

        System.out.println("Texto encontrado en el menu: " + enlaceBecas.innerText());

        enlaceBecas.click();

        //PASO 3. Esperar que cargue la pagina de Becas
        page.waitForLoadState(LoadState.NETWORKIDLE);

        System.out.println("URL actual despues del click: " + page.url());

        /************ Verificacion de la situacion esperada - Assert ***************/

        String urlActual = page.url().toLowerCase();
        String contenidoPagina = page.locator("body").innerText().toLowerCase();

        boolean contieneInformacionDeBecas =
                urlActual.contains("beca") || contenidoPagina.contains("beca");

        Assert.assertTrue(
                contieneInformacionDeBecas,
                "Resultado esperado: La pagina debe mostrar informacion relacionada con Becas."
        );

        System.out.println("Resultado esperado cumplido: Se visualiza informacion relacionada con Becas.");
    }

    @AfterTest
    public void tearDown() {

        if (page != null) {
            page.close();
        }

        if (context != null) {
            context.close();
        }

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}