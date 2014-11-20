package org.dbunit.datasets.dataset;

import java.util.HashMap;
import java.util.Map;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;

public class TimeStampReplaceColumnIdentifier {

	String datePattern = "cyyMMdd";
	String patternRegex = "[01]?[0-9][0-9](0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";

	public Map<String, TimeStampReplaceColumnInfo> identifyColumnInfo(final ITable table) throws DataSetException {
		final Map<String, TimeStampReplaceColumnInfo> retval = new HashMap<String, TimeStampReplaceColumnInfo>();
		columnLoop: for (final Column column : table.getTableMetaData().getColumns()) {
			for (int row = 0; row < table.getRowCount(); row++) {
				final Object value = table.getValue(row, column.getColumnName());
				if (value != null && !patternRegex.matches(value.toString())) {
					continue columnLoop;
				}
			}
			retval.put(column.getColumnName(), new TimeStampReplaceColumnInfo(column.getColumnName(), datePattern));
		}
		return retval;
	}

	public static class TimeStampReplaceColumnInfo {
		public TimeStampReplaceColumnInfo(final String column, final String pattern) {
			super();
			this.column = column;
			this.pattern = pattern;
		}

		String column;

		public String getColumn() {
			return column;
		}

		public String getPattern() {
			return pattern;
		}

		String pattern;
	}
}
