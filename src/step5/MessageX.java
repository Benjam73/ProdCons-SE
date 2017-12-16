package step5;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	// MessageX is the productionNumber-th produced by messageProducer
	private Producteur messageProducer;
	private Integer productionNumber;

	// MessageX is the consumptionNumber-th consumed by messageConsumer
	private Consommateur messageConsumer;
	private Integer consumptionNumber;

	// Unique id built using Producer and production number
	private String id;

	public MessageX(Producteur messageProducer) {
		super();
		this.messageProducer = messageProducer;
		this.productionNumber = messageProducer.alreadyProduced();
		this.id = "[" + messageProducer.identification() + ", " + getProductionNumber() + "]";
		this.messageConsumer = null;
		this.consumptionNumber = null;
	}

	public String toString() {
		return id;
	}

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
