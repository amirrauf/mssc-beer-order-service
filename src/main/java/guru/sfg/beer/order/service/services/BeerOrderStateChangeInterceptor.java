package guru.sfg.beer.order.service.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

	private final BeerOrderRepository beerOrderRepository;
	@Override
	public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
			Message<BeerOrderEventEnum> message, Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
			StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {
		/*Optional.ofNullable(message).ifPresent(msg -> {
			Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(BeerOrderManagerImpl.ORDER_ID_HEADER, -1L)))
			.ifPresent(beerOrderId -> {
				BeerOrder beerOrder = beerOrderRepository.getOne(beerOrderId);
				beerOrder.setState(state.getId());
				beerOrderRepository.save(beerOrder);
			});
		}); */
		
		Optional.ofNullable(message)
			.flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.ORDER_ID_HEADER, "")))
			.ifPresent(orderId -> {
				BeerOrder beerOrder = beerOrderRepository.getOne(UUID.fromString(orderId));
				beerOrder.setOrderStatus(state.getId());
				beerOrderRepository.saveAndFlush(beerOrder);
			});
		
	}

}
