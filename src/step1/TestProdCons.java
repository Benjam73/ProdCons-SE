package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {

	private int nbProd;
	private int nbCons;
	private int nbBuffer;
	private int tempsMoyenProduction;
	private int deviationTempsMoyenProduction;
	private int tempsMoyenConsommation;
	private int deviationTempsMoyenConsommation;
	private int nombreMoyenDeProduction;
	private int deviationNombreMoyenDeProduction;
	// Unused on V1
	private int nombreMoyenNbExemplaire;
	private int deviationNombreMoyenNbExemplaire;

	private ArrayList<Consommateur> consumer;
	private Aleatoire nbMessageToProduceRandomVariable;

	private ProdCons buffer;

	public TestProdCons(Observateur observateur) {

		super(observateur);
		consumer = new ArrayList<Consommateur>();
		try {
			init("options/options.xml");
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.getMessage();
		}
		buffer = new ProdCons(10);
		nbMessageToProduceRandomVariable = new Aleatoire(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
	}

	@Override
	protected void run() throws Exception {
		// Producer
		for (int i = 0; i < nbProd; i++) {
			int nbMessageToProduce = randomNumberOfMessageToProduce();
			Producteur producer = new Producteur(tempsMoyenProduction, deviationTempsMoyenProduction,
					nbMessageToProduce, this.getBuffer());
			producer.start();
		}
		// Consumer
		for (int i = 0; i < nbCons; i++) {
			Consommateur newConsumer = new Consommateur(tempsMoyenConsommation, deviationTempsMoyenConsommation,
					this.getBuffer());
			this.getConsumerList().add(newConsumer);
			newConsumer.start();
		}
	}

	private ProdCons getBuffer() {
		return this.buffer;
	}

	private ArrayList<Consommateur> getConsumerList() {
		return consumer;
	}

	private int randomNumberOfMessageToProduce() {
		return nbMessageToProduceRandomVariable.next();
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
		// try {
		// myTest.init(("options/" + "options.xml").toString());
		// } catch (IllegalArgumentException | IllegalAccessException |
		// NoSuchFieldException | SecurityException
		// | IOException e) {
		// e.getMessage();
		// e.printStackTrace();
		// }
		//
		// // Test MessageX toString
		// int moyenneTempsDeTraitement = 10;
		// int deviationTempsDeTraitement = 1;
		// Producteur prod;
		// Consommateur cons;
		// try {
		// System.out.println("Creating messageX mess, hence adding producer to
		// mess");
		// prod = new Producteur(moyenneTempsDeTraitement,
		// deviationTempsDeTraitement, 3, myTest.buffer);
		// MessageX mess = new MessageX(prod);
		// System.out.println("Mess : " + mess.toString());
		// System.out.println("Adding consummer to mess");
		// cons = new Consommateur(moyenneTempsDeTraitement,
		// deviationTempsDeTraitement, myTest.buffer);
		// mess.setMessageConsumer(cons);
		// System.out.println("Mess : " + mess.toString());
		// } catch (ControlException e) {
		// e.printStackTrace();
		// }
		// End(Test MessageX toString)
	}

}
