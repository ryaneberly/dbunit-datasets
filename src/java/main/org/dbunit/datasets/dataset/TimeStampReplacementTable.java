package org.dbunit.datasets.dataset;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.datasets.dataset.TimeStampReplaceColumnIdentifier.TimeStampReplaceColumnInfo;
import org.dbunit.datasets.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Decorator that replaces configured values from the decorated table with
 * replacement values.
 *
 * @author Ryan Eberly
 * @since Nov 5, 2014

 */
public class TimeStampReplacementTable implements ITable {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(TimeStampReplacementTable.class);

	private final ITable _table;

	private final int days;

	private Map<String, TimeStampReplaceColumnInfo> dateColumns;

	/**
	 * Create a new ReplacementTable object that decorates the specified table.
	 *
	 * @param table
	 *            the decorated table
	 * @throws DataSetException
	 */
	// public EmbeddedScriptReplacementTable(ITable table)
	// {
	// this(table, new HashMap(), new HashMap(), null, null);
	// }

	public TimeStampReplacementTable(final ITable table, final int days)  {
		_table = table;
		this.days = days;
		try {
			dateColumns = new TimeStampReplaceColumnIdentifier().identifyColumnInfo(table);
		} catch (DataSetException e) {
			e.printStackTrace();
			dateColumns = new HashMap<String, TimeStampReplaceColumnInfo>();
		}
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

		// String tableAndColumn = _table.getTableMetaData().getTableName()+"."
		// + column;
		// String pattern = "PPP905.ST??DT";
		// String datePattern = "yyMMdd";
		final Object value = _table.getValue(row, column);
		if (dateColumns.containsKey(column) && value != null && value.toString().trim().length() > 0) {
			final String datePattern = dateColumns.get(column).getPattern();
			final Date oldDate = DateUtils.readDate(value.toString().trim(), datePattern);
			final Date newDate = DateUtils.addDays(oldDate, days);
			return DateUtils.convertDate(newDate, datePattern);
		} else {
			return value;
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append("[");
		sb.append("dateColumns=");
		for (final String datekey : dateColumns.keySet()) {
			sb.append(datekey).append(",");
		}
		sb.append("]");
		return sb.toString();
	}
}
