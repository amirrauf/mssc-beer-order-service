package guru.sfg.beer.order.service.services.listeners;

import java.util.UUID;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationResultListener {
	
	private final BeerOrderManager beerOrderManager;
	
	@JmsListener(destination=JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
	public void listen(ValidateOrderResult validateOrderResult) {
		final UUID beerOrderId = validateOrderResult.getOrderId();
		
		log.debug("Validate Result for order ID" + beerOrderId);
		
		beerOrderManager.processValidationResult(beerOrderId, validateOrderResult.getIsValid());
	}

}
