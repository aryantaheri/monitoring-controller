/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.netconf.cli.reader;

import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;

/**
 * marker interface to mark reader which can be used with GenericListReader
 */
public interface GenericListEntryReader<T extends DataSchemaNode> extends Reader<T> {

}
