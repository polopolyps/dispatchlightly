package com.polopoly.ps.dispatchlightly.sz;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.junit.Test;

import com.polopoly.ps.dispatchlightly.DefaultModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.VelocityInitializer;
import com.polopoly.ps.dispatchlightly.model.SimpleLightURLBuilder;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.ps.dispatchlightly.render.DefaultRenderRequest;
import com.polopoly.ps.dispatchlightly.render.RenderRequest;
import com.polopoly.ps.dispatchlightly.render.Renderer;

public class StartPageRenderingTest {

	@Test
	public void testRender() throws Exception {
		new VelocityInitializer().initialize();

		Writer writer = new FileWriter(new File("sueddeutsche.de.html"));

		DefaultModelContext context = new DefaultModelContext();

		StartPage startPage = createStartPage();

		context.put(startPage);
		context.put(context);
		context.put(new SimpleLightURLBuilder());

		RenderRequest request = new DefaultRenderRequest((Class<? extends Model>) StartPageModel.class,
				RenderMode.DEFAULT, context);

		new Renderer(request).render(writer);

		writer.close();
	}

	private StartPage createStartPage() {
		StartPagePojo result = new StartPagePojo();

		createMainArticle(result);

		createMainSlot(result);

		return result;
	}

	private void createMainSlot(StartPagePojo startPage) {
		startPage.addInMainSlot(createFirstMainSlotArticle());
		startPage.addInMainSlot(createSecondMainSlotArticle());
	}

	protected ArticlePlacementPojo createFirstMainSlotArticle() {
		ArticlePojo libyasFuture = new ArticlePojo(
				"Libyens Zukunft nach Gaddafis Tod",
				"Wenn der gemeinsame Feind fehlt",
				"http://www.sueddeutsche.de/politik/libyens-zukunft-nach-gaddafis-tod-wenn-der-gemeinsame-feind-fehlt-1.1170600");

		libyasFuture
				.setIntro("Nur Tr&auml;umer k&ouml;nnen ernsthaft glauben, dass sich Libyen nach Gaddafis Tod zu einer Demokratie nach "
						+ "westlichem Vorbild wandelt. Die Rebellen waren sich allein im Widerstand gegen den Ex-Diktator einig - am Ende wird wohl nur der "
						+ "politische Islam das Land befrieden k&ouml;nnen. ");

		libyasFuture.setAuthor("Rudolph Chimelli");

		libyasFuture
				.addOneliner(new ArticlePojo(
						"Einmaliger Erfolg - dank Gaddafi",
						"Nato-Einsatz in Libyen",
						"http://www.sueddeutsche.de/politik/erfolgreicher-nato-einsatz-in-libyen-einmaliger-erfolg-dank-gaddafi-1.1170580"));

		libyasFuture
				.setImage(new ImagePojo(
						208,
						156,
						"http://polpix.sueddeutsche.com:80/polopoly_fs/1.1170654.1319210618!/image/image.jpg_gen/derivatives/208x156_fit/image.jpg"));

		ArticlePojo speculations = new ArticlePojo(
				"Spekulationen um die letzten Stunden des Diktators",
				"Gaddafis Tod ",
				"http://www.sueddeutsche.de/politik/gaddafis-tod-spekulationen-um-die-letzten-stunden-des-diktators-1.1169868");
		speculations.setOpinion(true);

		libyasFuture.addOneliner(speculations);

		ArticlePlacementPojo libyasFuturePlacement = new ArticlePlacementPojo(libyasFuture);

		libyasFuturePlacement
				.addButton(new ButtonPojo(
						ButtonType.IMAGE,
						"http://www.sueddeutsche.de/politik/libyens-gestuerzter-machthaber-gaddafi-bunte-stoffe-und-ein-gewehr-im-anschlag-1.1135737"));
		libyasFuturePlacement
				.addButton(new ButtonPojo(ButtonType.VIDEO,
						"http://www.sueddeutsche.de/politik/gaddafi-reax-o-reaktionen-auf-den-tod-gaddafis-1.1169770"));
		return libyasFuturePlacement;
	}

	protected ArticlePlacementPojo createSecondMainSlotArticle() {
		ArticlePojo screenshot = new ArticlePojo("Ein Screenshot hat keine Würde",
				"Bilder des getöteten Gaddafi",
				"http://www.sueddeutsche.de/kultur/bilder-des-getoeteten-gaddafi-ein-screenshot-hat-keine-wuerde-1.1170440");

		screenshot
				.setIntro("Wie umgehen mit den Bildern des getöteten libyschen Diktators Gaddafi? Sind sie historische Dokumente, "
						+ "die belegen, dass eine Tyrannei endlich zu Ende gegangen ist - oder bedienen sie nur den archaischen Wunsch "
						+ "nach einer Trophäe? Die Würde eines Toten sollte geachtet und respektiert werden - selbst dann, wenn es sich "
						+ "bei diesen Bildern um Aufnahmen eines toten Diktators handelt, der sich selber zu seinen Lebzeiten wenig um "
						+ "diese Würde geschert hat.");

		screenshot.setOpinion(true);
		screenshot.setAuthorPrefix("Ein Kommentar von");
		screenshot.setAuthor("Bernd Graff");

		screenshot.addOneliner(new ArticlePojo("Ende einer Legende", "Libyen nach Gaddafis Tod",
				"http://www.sueddeutsche.de/politik/libyen-nach-gaddafis-tod-ende-einer-legende-1.1169675"));

		screenshot
				.addOneliner(new ArticlePojo("Das Antlitz des Todes", "Bilder getöteteter Erzfeinde",
						"http://www.sueddeutsche.de/kultur/zum-bild-des-getoeteten-bin-laden-das-antlitz-des-todes-1.1092258"));

		ArticlePlacementPojo screenshotPlacement = new ArticlePlacementPojo(screenshot);

		return screenshotPlacement;
	}

	private void createMainArticle(StartPagePojo result) {
		ArticlePojo mainArticle = new ArticlePojo("Obama beendet Irak-Einsatz zum Jahresende",
				"US-Truppenabzug",
				"http://www.sueddeutsche.de/politik/us-truppenabzug-obama-beendet-irak-einsatz-zum-jahresende-1.1170737");
		mainArticle
				.setIntro("Der Irak-Krieg ist offiziell beendet: US-Präsident Obama will bis zum Jahresende die verbliebenen 40.000 Soldaten vollständig abziehen. Er versucht, diesen Schritt als Triumph seiner Politik darzustellen, der \"in voller Übereinstimmung\" mit der irakischen Regierung geschehe. Das allerdings dürfte nur die halbe Wahrheit sein.");
		mainArticle.setAuthor("Von Reymer Klöver, Washington");
		mainArticle
				.addOneliner(new ArticlePojo("Krieg ohne Gewinner", "Abzug der US-Truppen aus dem Irak",
						"http://www.sueddeutsche.de/politik/abzug-der-us-truppen-aus-dem-irak-krieg-ohne-gewinner-1.1157765"));

		mainArticle
				.setImage(new ImagePojo(
						860,
						301,
						"http://polpix.sueddeutsche.com:80/polopoly_fs/1.1170740.1319220816!/image/image.jpg_gen/derivatives/860x301/image.jpg"));

		result.setMainArticle(mainArticle);
	}
}
