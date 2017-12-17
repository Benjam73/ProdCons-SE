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

	public synchronized void messageDeposition(Producteur producer, Message message) throws ControlException {
		if (producer == null) {
			coherence = false;
		}
		if (!producerList.contains(producer)) {
			coherence = false;
			System.out.println("2");
		}
		if (message == null) {
			coherence = false;
			System.out.println("3");
		}
		if (!((MessageX) message).getMessageProducer().equals(producer)) {
			coherence = false;
			System.out.println("4");
		}
		if (!productionList.contains(message)) {
			coherence = false;
			System.out.println("5");
		}

		globalBuffer.add(message);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "messageDeposition");
		}

	}

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

	public boolean finalCoherence() {
		if (globalBuffer.size() == 0 && coherence) {
			return true;
		} else {
			return false;
		}
	}

}
