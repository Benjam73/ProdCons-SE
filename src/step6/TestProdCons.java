package step6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
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

	private List<Consommateur> consumerList;
	private List<Producteur> producerList;

	private Aleatoire nbMessageToProduceRandomVariable;

	private ProdCons buffer;

	public TestProdCons(Observateur observateur) {

		super(observateur);
		consumerList = new ArrayList<Consommateur>();
		producerList = new ArrayList<Producteur>();

		try {
			init("options/options.xml");
			observateur.init(nbProd, nbCons, nbBuffer);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException | ControlException e) {
			e.getMessage();
		}
		buffer = new ProdCons(nbBuffer);
		nbMessageToProduceRandomVariable = new Aleatoire(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
	}

	@Override
	protected void run() throws Exception {
		// Producer
		for (int i = 0; i < nbProd; i++) {
			int nbMessageToProduce = randomNumberOfMessageToProduce();
			Producteur newProducer = new Producteur(this, observateur, tempsMoyenProduction,
					deviationTempsMoyenProduction, nbMessageToProduce, this.getBuffer());
			observateur.newProducteur(newProducer);
			producerList.add(newProducer);
			newProducer.start();
		}
		// Consumer
		for (int i = 0; i < nbCons; i++) {
			Consommateur newConsumer = new Consommateur(this, observateur, tempsMoyenConsommation,
					deviationTempsMoyenConsommation, this.getBuffer());
			observateur.newConsommateur(newConsumer);
			this.getConsumerList().add(newConsumer);
			newConsumer.start();
		}
	}

	private ProdCons getBuffer() {
		return this.buffer;
	}

	private List<Consommateur> getConsumerList() {
		return consumerList;
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
	}

	public void removeProducer(Producteur producteur) {
		producerList.remove(producteur);

	}

	public boolean hasProducer() {
		return !(producerList.isEmpty());
	}

}