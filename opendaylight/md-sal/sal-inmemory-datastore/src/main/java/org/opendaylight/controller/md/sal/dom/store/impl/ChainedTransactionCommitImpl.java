/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.sal.dom.store.impl;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.opendaylight.controller.sal.core.spi.data.DOMStoreThreePhaseCommitCohort;
import org.opendaylight.controller.sal.core.spi.data.ForwardingDOMStoreThreePhaseCommitCohort;

final class ChainedTransactionCommitImpl extends ForwardingDOMStoreThreePhaseCommitCohort {
    private final SnapshotBackedWriteTransaction transaction;
    private final DOMStoreThreePhaseCommitCohort delegate;
    private final DOMStoreTransactionChainImpl txChain;

    ChainedTransactionCommitImpl(final SnapshotBackedWriteTransaction transaction,
            final DOMStoreThreePhaseCommitCohort delegate, final DOMStoreTransactionChainImpl txChain) {
        this.transaction = Preconditions.checkNotNull(transaction);
        this.delegate = Preconditions.checkNotNull(delegate);
        this.txChain = Preconditions.checkNotNull(txChain);
    }

    @Override
    protected DOMStoreThreePhaseCommitCohort delegate() {
        return delegate;
    }

    @Override
    public ListenableFuture<Void> commit() {
        ListenableFuture<Void> commitFuture = super.commit();
        Futures.addCallback(commitFuture, new FutureCallback<Void>() {
            @Override
            public void onFailure(final Throwable t) {
                txChain.onTransactionFailed(transaction, t);
            }

            @Override
            public void onSuccess(final Void result) {
                txChain.onTransactionCommited(transaction);
            }
        });
        return commitFuture;
    }

}