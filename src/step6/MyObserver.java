package step6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;

public class MyObserver {

	private boolean coherence;
	private int nbProducer;
	private int nbConsumer;
	private int bufferSize;
	private List<Message> productionList;
	private List<Message> consumptionList;
	private List<Consommateur> consumerList;
	private List<Producteur> producerList;
	private Queue<Message> globalBuffer;
	private int consumeNumber;
	private int productionNumber;

	public MyObserver() {
		coherence = true;
	}

	public boolean getCoherence() {
		return this.coherence;
	}

	/**
	 * 
	 * @param nbProducer
	 *            Number of Producteur created by TestProdCons
	 * @param nbConsumer
	 *            Number of Consommateur created by TestProdCons
	 * @param bufferSize
	 *            The size of the ProdCons buffer used
	 * @throws ControlException
	 */
	public void init(int nbProducer, int nbConsumer, int bufferSize) throws ControlException {
		if (nbProducer < 0 || nbConsumer < 0 || bufferSize < 0) {
			coherence = false;
		}
		this.nbProducer = nbProducer;
		this.nbConsumer = nbConsumer;
		this.bufferSize = bufferSize;

		producerList = new ArrayList<Producteur>();
		consumerList = new ArrayList<Consommateur>();

		productionList = new ArrayList<Message>();
		consumptionList = new ArrayList<Message>();

		globalBuffer = new LinkedList<Message>();

		productionNumber = 0;
		consumeNumber = 0;

		if (!coherence) {
			throw new ControlException(this.getClass(), "init");
		}
	}

	/**
	 * Called when a new Producteur is created
	 * 
	 * @param newProducer
	 *            The new Producteur created
	 * @throws ControlException
	 */
	public synchronized void newProducer(Producteur newProducer) throws ControlException {
		if (newProducer == null) {
			coherence = false;
		}
		if (producerList.contains(newProducer)) {
			System.out.println("test : " + getCoherence());
			coherence = false;
		}
		if (producerList.size() >= nbProducer) {

			coherence = false;
		}

		producerList.add(newProducer);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "newProducer");
		}
	}

	/**
	 * Called when a new Consommateur si created
	 * 
	 * @param newConsumer
	 *            The new Consommateur created
	 * @throws ControlException
	 */
	public synchronized void newConsumer(Consommateur newConsumer) throws ControlException {
		if (newConsumer == null) {
			coherence = false;
		}
		if (consumerList.contains(newConsumer)) {
			coherence = false;
		}
		if (consumerList.size() >= nbConsumer) {
			coherence = false;
		}

		consumerList.add(newConsumer);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "newConsumer");
		}
	}

	/**
	 * Called when a Producteur create a new message
	 * 
	 * @param producer
	 *            The producteur who create the message
	 * @param message
	 *            The message created by the Producteur
	 * @param duration
	 *            The duration time required to create the message
	 * @throws ControlException
	 */
	public synchronized void messageProduction(Producteur producer, Message message, int duration)
			throws ControlException {
		if (producer == null) {
			coherence = false;
		}
		if (!producerList.contains(producer)) {
			coherence = false;
		}
		if (message == null) {
			coherence = false;
		}
		if (duration <= 0) {
			coherence = false;
		}
		if (!((MessageX) message).getMessageProducer().equals(producer)) {
			coherence = false;
		}

		productionList.add(message);
		productionNumber++;

		if (coherence == false) {
			throw new ControlException(this.getClass(), "messageProduction");
		}
	}

	/**
	 * Called when a Consommateur consume a message
	 * 
	 * @param consumer
	 *            The Consommateur who has consumed the message
	 * @param message
	 *            The message consumed
	 * @param duration
	 *            The duration required to consume the message
	 * @throws ControlException
	 */
	public synchronized void messageConsumption(Consommateur consumer, Message message, int duration)
			throws ControlException {
		if (consumer == null) {
			coherence = false;
		}
		if (!consumerList.contains(consumer)) {
			coherence = false;
		}
		if (!productionList.contains(message)) {
			coherence = false;
		}
		if (message == null) {
			coherence = false;
		}
		if (duration <= 0) {
			coherence = false;
		}

		consumptionList.add(message);
		consumeNumber++;

		if (coherence == false) {
			throw new ControlException(this.getClass(), "messageConsumption");
		}
	}

	/**
	 * Called when a producteur put a message in the ProdCons buffer
	 * 
	 * @param producer
	 *            The Producteur who put the message
	 * @param message
	 *            The put message
	 * @throws ControlException
	 */
	public synchronized void messageDeposition(Producteur producer, Message message) throws ControlException {
		if (producer == null) {
			coherence = false;
		}
		if (!producerList.contains(producer)) {
			coherence = false;
		}
		if (message == null) {
			coherence = false;
		}
		if (!((MessageX) message).getMessageProducer().equals(producer)) {
			coherence = false;
		}
		if (!productionList.contains(message)) {
			coherence = false;
		}

		globalBuffer.add(message);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "messageDeposition");
		}

	}

	/**
	 * Called when a Consommateur get a message from the ProdCons buffer
	 * 
	 * @param consumer
	 *            The Consommateur who get the message
	 * @param message
	 *            The getted message
	 * @throws ControlException
	 */
	public synchronized void messageRemoved(Consommateur consumer, Message message) throws ControlException {
		if (consumer == null) {
			coherence = false;

		}
		if (!consumerList.contains(consumer)) {
			coherence = false;

		}
		if (!productionList.contains(message)) {
			coherence = false;

		}
		if (message == null) {
			coherence = false;

		}

		globalBuffer.remove(message);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "messageRemoved");
		}

	}

	/**
	 * Called at the end of the program to check if the final coherence is OK
	 * 
	 * @return true if all the message created have been consumed and the all
	 *         along coherence has been kept otherwise false
	 */
	public boolean finalCoherence() {
		if (globalBuffer.size() == 0 && coherence) {
			return true;
		} else {
			return false;
		}
	}

}
