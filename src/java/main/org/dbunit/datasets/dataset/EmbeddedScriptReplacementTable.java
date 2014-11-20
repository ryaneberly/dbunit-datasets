package org.dbunit.datasets.dataset;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Decorator that replaces configured values from the decorated table with
 * replacement values.
 *
 * @author Ryan Eberly
 * @since Nov 5, 2014
 */
public class EmbeddedScriptReplacementTable implements ITable {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(EmbeddedScriptReplacementTable.class);

	private final ITable _table;
	private final ScriptEngine engine;

	/**
	 * Create a new ReplacementTable object that decorates the specified table.
	 *
	 * @param table
	 *            the decorated table
	 */
	// public EmbeddedScriptReplacementTable(ITable table)
	// {
	// this(table, new HashMap(), new HashMap(), null, null);
	// }

	public EmbeddedScriptReplacementTable(final ITable table, final ScriptEngine engine) {
		_table = table;
		this.engine = engine;
	}

	// //////////////////////////////////////////////////////////////////////
	// ITable interface

	public ITableMetaData getTableMetaData() {
		return _table.getTableMetaData();
	}

	public int getRowCount() {
		return _table.getRowCount();
	}

	public Object getValue(final int row, final String column) throws DataSetException {
		if (logger.isDebugEnabled()) {
			logger.debug("getValue(row={}, columnName={}) - start", Integer.toString(row), column);
		}

		final Object value = _table.getValue(row, column);
		if (value != null && value instanceof String) {
			final String stringvalue = ((String) value).trim();
			if (stringvalue.startsWith("${") || stringvalue.endsWith("}")) {
				final String script = stringvalue.replaceFirst("\\$\\{", "").replaceFirst("\\}$", "");
				try {
					return engine.eval(script);
				} catch (final ScriptException e) {
					logger.error("Error script", e);
					throw new DataSetException(e);
				}
			} else {
				return value;
			}
		} else {
			return value;
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append("[");
		sb.append("engine=").append(engine);
		sb.append("]");
		return sb.toString();
	}
}
