package query

import events.TodoCreatedEvent
import events.TodoMarkedAsCompleteEvent
import org.axonframework.domain.EventMessage
import org.axonframework.eventhandling.EventListenerProxy
import org.vertx.groovy.deploy.Container
import org.vertx.groovy.core.eventbus.EventBus

/**
 * Listener that handles all events and creates messages to store the todoItems in the query database.
 *
 * @author Jettro Coenradie
 */
class TodoEventListener implements EventListenerProxy {
    Container container
    EventBus eventBus
    def logger

    TodoEventListener(EventBus eventBus, Container container) {
        this.container = container
        this.logger = container.logger
        this.eventBus = eventBus
    }

    Class<?> getTargetType() {
        return TodoCreatedEvent.class
    }

    void handle(EventMessage eventMessage) {
        def identifier = eventMessage.getIdentifier()
        def eventType = eventMessage.payloadType

        logger.debug "Received an event with id ${identifier} and type ${eventType}"

        switch (eventType) {
            case TodoCreatedEvent.class:
                logger.info "Received a TodoCreatedEvent"
                def event = eventMessage.payload as TodoCreatedEvent
                eventBus.publish("message.all.clients",["todoText": event.todoText,"identifier":event.identifier])
                break
            case TodoMarkedAsCompleteEvent.class:
                logger.info "Received a TodoMarkedAsCompleteEvent"
                def event = eventMessage.payload as TodoMarkedAsCompleteEvent
                // TODO implement this functionality
                break
            default:
                logger.info "Cannot handle this event"
        }
    }
}
