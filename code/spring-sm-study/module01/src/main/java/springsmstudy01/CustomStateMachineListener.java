package springsmstudy01;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class CustomStateMachineListener<S, E> extends StateMachineListenerAdapter<S, E> {

    @Override
    public void eventNotAccepted(Message<E> event) {
        System.out.println("Event not accepted: " + event.getPayload());
    }

    @Override
    public void stateChanged(State<S, E> from, State<S, E> to) {
        System.out.println("State changed from " + getIdOrNull(from) + " to " + getIdOrNull(to));
    }

    private S getIdOrNull(State<S, E> state) {
        return state == null ? null : state.getId();
    }
}
