package step2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import common.Debugger;
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
	// Unused on step2
	private int nombreMoyenNbExemplaire;
	private int deviationNombreMoyenNbExemplaire;

	private List<Consommateur> consumerList;
	private List<Producteur> producerList;

	private Aleatoire nbMessageToProduceRandomVariable;

	private ProdCons buffer;

	private List<Consommateur> consumerThreadList;
	private List<Producteur> producerThreadList;

	/**
	 * 
	 * @param observateur
	 *            Default Observateur
	 */
	public TestProdCons(Observateur observateur) {

		super(observateur);
		consumerList = new ArrayList<Consommateur>();
		producerList = new ArrayList<Producteur>();

		producerThreadList = new ArrayList<Producteur>();
		consumerThreadList = new ArrayList<Consommateur>();

		try {
			init("options/test6.xml");
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| IOException e) {
			e.getMessage();
		}
		buffer = new ProdCons(nbBuffer);
		nbMessageToProduceRandomVariable = new Aleatoire(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
	}

	/**
	 * Main loop of the program, create, launch and wait for all the thread to
	 * finish
	 */
	@Override
	protected void run() throws Exception {
		Debugger.log("Number of producers : " + nbProd);
		Debugger.log("Number of consumers : " + nbCons);
		// Producer
		for (int i = 0; i < nbProd; i++) {
			int nbMessageToProduce = randomNumberOfMessageToProduce();
			Producteur newProducer = new Producteur(this, tempsMoyenProduction, deviationTempsMoyenProduction,
					nbMessageToProduce, this.getBuffer());
			producerList.add(newProducer);
			producerThreadList.add(newProducer);
			Debugger.log(newProducer.toString() + " will produce " + newProducer.nombreDeMessages() + " messages. ");
			newProducer.start();
		}
		// Consumer
		for (int i = 0; i < nbCons; i++) {
			Consommateur newConsumer = new Consommateur(this, tempsMoyenConsommation, deviationTempsMoyenConsommation,
					this.getBuffer());
			this.getConsumerList().add(newConsumer);
			consumerThreadList.add(newConsumer);
			newConsumer.start();
		}

		for (Iterator<Producteur> iterator = producerThreadList.iterator(); iterator.hasNext();) {
			Producteur currentProducer = iterator.next();
			currentProducer.join();
		}

		/* wait for consommateur termination */
		while (buffer.enAttente() > 0) {
			Thread.yield();
		}

		/* Interruption of sleeping consommateur */
		for (Iterator<Consommateur> iterator = consumerThreadList.iterator(); iterator.hasNext();) {

			Consommateur currentConsumer = iterator.next();
			currentConsumer.interrupt();
			currentConsumer.join();

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

	/**
	 * Given method
	 */
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

	/**
	 * 
	 * @param args
	 *            The program arguments : here not arguments are needed
	 */
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
