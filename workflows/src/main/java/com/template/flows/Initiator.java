package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.IOUContract;
import com.template.states.IOUState;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Arrays;

@InitiatingFlow
@StartableByRPC
public class Initiator extends FlowLogic<Void> {

    private final int iouValue;
    private final Party otherParty;
    private final ProgressTracker progressTracker = new ProgressTracker();

    public Initiator(int iouValue, Party otherParty) {
        this.iouValue = iouValue;
        this.otherParty = otherParty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {

        final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        final IOUState output = new IOUState(iouValue, getOurIdentity(), otherParty);

        Command command = new Command(new IOUContract.Create(), Arrays.asList(getOurIdentity().getOwningKey(), otherParty.getOwningKey()));

        final TransactionBuilder builder = new TransactionBuilder(notary);
        builder.addOutputState(output);
        builder.addCommand(command);
        builder.verify(getServiceHub());


        final SignedTransaction signedTx = getServiceHub().signInitialTransaction(builder);

        FlowSession otherPartSession = initiateFlow(otherParty);

        SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(signedTx, Arrays.asList(otherPartSession), CollectSignaturesFlow.tracker()));

        subFlow(new FinalityFlow(fullySignedTx, otherPartSession));


        return null;

    }
}