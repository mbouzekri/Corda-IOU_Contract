package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.states.IOUState;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireThat;

@InitiatedBy(Initiator.class)
public class Responder extends FlowLogic<Void> {

    private FlowSession otherPartySession;

    public Responder(FlowSession otherPartySession) {
        this.otherPartySession = otherPartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {

        class SignTxFlow extends SignTransactionFlow {
            public SignTxFlow(@NotNull FlowSession otherSideSession) {
                super(otherSideSession);
            }
            @Override
            protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                requireThat(require -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    require.using("Transaction must be of type IOUState", output instanceof IOUState);
                    IOUState iou = (IOUState) output;
                    require.using("Minimum amount is $100", iou.getValue() >= 100);
                    return null;
                });
            }
        }

        SecureHash expectedTxId = subFlow(new SignTxFlow(otherPartySession)).getId();


        subFlow(new ReceiveFinalityFlow(otherPartySession, expectedTxId));
        return null;
    }
}
