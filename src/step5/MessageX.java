package step5;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	// null if messageX ain't in buffer yet, its global buffer order rank
	// otherwise
	private Integer messageXNumber = null;

	// MessageX is the productionNumber-th produced by messageProducer
	private Producteur messageProducer;
	private Integer productionNumber;

	// MessageX is the consumptionNumber-th consumed by messageConsumer
	private Consommateur messageConsumer;
	private Integer consumptionNumber;

	// Unique id built using Producer and production number
	private String id;

	/**
	 * 
	 * @param messageProducer
	 *            The Producteur who has created the new MessageX
	 */
	public MessageX(Producteur messageProducer) {
		super();
		this.messageProducer = messageProducer;
		this.productionNumber = messageProducer.alreadyProduced();
		this.id = "[" + messageProducer.identification() + ", " + getProductionNumber() + "]";
		this.messageConsumer = null;
		this.consumptionNumber = null;
	}

	/**
	 * @return The String conversion of a MessageX
	 */
	public String toString() {
		String res;
		if (messageXNumber != null) {
			res = "[" + messageXNumber.toString() + ", " + messageProducer.identification() + ", "
					+ getProductionNumber() + "]";
		} else {
			res = id;
		}
		return res;
	}

	/**
	 * 
	 * @return The historic of the Message, who has produced it and who has
	 *         consumed it
	 */
	public String history() {
		String resultString = new String();
		resultString = resultString.concat(
				getProductionNumber().toString() + "-th message of messageProducer " + getMessageProducer().toString());
		if (messageConsumer != null) {
			resultString = resultString.concat(", " + getConsumptionNumber().toString() + "-th consummed by "
					+ getMessageConsumer().toString() + ". ");
		} else {
			resultString = resultString.concat(", not consummed yet. ");
		}
		return resultString;
	}

	// Setters
	public void setMessageConsumer(Consommateur messageConsumer) {
		this.messageConsumer = messageConsumer;
		setConsumptionNumber(messageConsumer.nombreDeMessages());
	}

	private void setConsumptionNumber(Integer consumptionNumber) {
		this.consumptionNumber = consumptionNumber;
	}

	public void setId(Integer msgNumber) {
		messageXNumber = msgNumber;
	}

	// Getters
	public Producteur getMessageProducer() {
		return messageProducer;
	}

	public Integer getProductionNumber() {
		return productionNumber;
	}

	public Consommateur getMessageConsumer() {
		return messageConsumer;
	}

	public Integer getConsumptionNumber() {
		return consumptionNumber;
	}
}
