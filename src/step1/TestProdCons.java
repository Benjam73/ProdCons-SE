package step1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {

	int nbProd;
	int nbCons;
	int nbBuffer;
	int tempsMoyenProduction;
	int deviationTempsMoyenProduction;
	int tempsMoyenConsommation;
	int deviationTempsMoyenConsommation;
	int nombreMoyenDeProduction;
	int deviationNombreMoyenDeProduction;
	int nombreMoyenNbExemplaire;
	int deviationNombreMoyenNbExemplaire;

	protected ProdCons buffer;

	public TestProdCons(Observateur observateur) {

		super(observateur);
		try {
			init("options/options.xml");
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.getMessage();
		}

		buffer = new ProdCons();
	}

	protected void init(String file) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, InvalidPropertiesFormatException, IOException {
		Properties properties = new Properties();
		properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			key = (String) entry.getKey();
			value = Integer.parseInt((String) entry.getValue());
			thisOne.getDeclaredField(key).set(this, value);
		}
	}

	public static void main(String[] args) {
		TestProdCons myTest = new TestProdCons(new Observateur());
		myTest.start();
		try {
			myTest.init(("options/" + "options.xml").toString());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.getMessage();
			e.printStackTrace();
		}

		// Test MessageX toString
		int moyenneTempsDeTraitement = 10;
		int deviationTempsDeTraitement = 1;
		Producteur prod;
		Consommateur cons;
		try {
			System.out.println("Creating messageX mess, hence adding producer to mess");
			prod = new Producteur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
			MessageX mess = new MessageX(prod);
			System.out.println("Mess : " + mess.toString());
			System.out.println("Adding consummer to mess");
			cons = new Consommateur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
			mess.setMessageConsumer(cons);
			System.out.println("Mess : " + mess.toString());
		} catch (ControlException e) {
			e.printStackTrace();
		}
		// End(Test MessageX toString)
	}

	@Override
	protected void run() throws Exception {
		// TODO Auto-generated method stub

	}
}
