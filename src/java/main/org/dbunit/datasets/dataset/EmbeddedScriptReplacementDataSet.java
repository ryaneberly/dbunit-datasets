/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.dbunit.datasets.dataset;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Decorator that replace configured values from the decorated dataset with
 * evaluated script values values.
 *
 * @author Ryan Eberly
 * @since Nov 4, 2014
 */
public class EmbeddedScriptReplacementDataSet extends AbstractDataSet {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(EmbeddedScriptReplacementDataSet.class);

	private final IDataSet _dataSet;
	private final ScriptEngine engine;

	/**
	 * Create a new ReplacementDataSet object that decorates the specified
	 * dataset.
	 *
	 * @param dataSet
	 *            the decorated table
	 */
	public EmbeddedScriptReplacementDataSet(final IDataSet dataSet) {
		this(dataSet, "JavaScript");
	}

	/**
	 * 
	 * @param dataSet
	 * @param language
	 */
	public EmbeddedScriptReplacementDataSet(final IDataSet dataSet, final String language) {
		_dataSet = dataSet;
		final ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		this.engine = factory.getEngineByName(language);

	}

	private EmbeddedScriptReplacementTable createEmbeddedScriptReplacementTable(final ITable table) {
		logger.debug("createReplacementTable(table={}) - start", table);

		final EmbeddedScriptReplacementTable replacementTable = new EmbeddedScriptReplacementTable(table, engine);
		return replacementTable;
	}

	// //////////////////////////////////////////////////////////////////////////
	// AbstractDataSet class

	@Override
	protected ITableIterator createIterator(final boolean reversed) throws DataSetException {
		if (logger.isDebugEnabled()) {
			logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));
		}

		return new EmbeddedScriptReplacementIterator(reversed ? _dataSet.reverseIterator() : _dataSet.iterator());
	}

	// //////////////////////////////////////////////////////////////////////////
	// IDataSet interface

	@Override
	public String[] getTableNames() throws DataSetException {
		logger.debug("getTableNames() - start");

		return _dataSet.getTableNames();
	}

	@Override
	public ITableMetaData getTableMetaData(final String tableName) throws DataSetException {
		logger.debug("getTableMetaData(tableName={}) - start", tableName);

		return _dataSet.getTableMetaData(tableName);
	}

	@Override
	public ITable getTable(final String tableName) throws DataSetException {
		logger.debug("getTable(tableName={}) - start", tableName);

		return createEmbeddedScriptReplacementTable(_dataSet.getTable(tableName));
	}

	// //////////////////////////////////////////////////////////////////////////
	// ReplacementIterator class

	private class EmbeddedScriptReplacementIterator implements ITableIterator {

		/**
		 * Logger for this class
		 */
		private final Logger logger = LoggerFactory.getLogger(EmbeddedScriptReplacementIterator.class);

		private final ITableIterator _iterator;

		public EmbeddedScriptReplacementIterator(final ITableIterator iterator) {
			_iterator = iterator;
		}

		// //////////////////////////////////////////////////////////////////////
		// ITableIterator interface

		public boolean next() throws DataSetException {
			logger.debug("next() - start");

			return _iterator.next();
		}

		public ITableMetaData getTableMetaData() throws DataSetException {
			logger.debug("getTableMetaData() - start");

			return _iterator.getTableMetaData();
		}

		public ITable getTable() throws DataSetException {
			logger.debug("getTable() - start");

			return createEmbeddedScriptReplacementTable(_iterator.getTable());
		}
	}
}
