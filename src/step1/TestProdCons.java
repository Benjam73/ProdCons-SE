package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

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

	protected ArrayList<Consommateur> consumer;
	protected ArrayList<Producteur> producer;

	protected ProdCons buffer;

	public TestProdCons(Observateur observateur) {
		super(observateur);
		// TODO Auto-generated constructor stub
		consumer = new ArrayList<Consommateur>();
		producer = new ArrayList<Producteur>();
		try {
			init("options/options.xml");
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.getMessage();
		}

		buffer = new ProdCons();
	}

	@Override
	protected void run() throws Exception {
		// TODO Auto-generated method stub
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
	}
}
