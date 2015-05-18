package org.dbunit.datasets.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.Period;
import org.joda.time.PeriodType;


public class DateUtils {

	public static Date smartDate(String input) {
		if (input == null || input.trim().length() == 0) {
			return null;
		}
		input = input.trim();
		try {
			if (input.matches("0*")) {
				return null;
			}
			if (input.matches("[0-1]{2}[0-9]{5}") || input.matches("[0-1][0-9]{5}") || input.matches("[0-9]{5}")) { 
				input = convertDate(input, "cyyMMdd", "MM/dd/yyyy");
			}
			if (input.matches("^....-..?-..*$")) {
				final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
				return ft.parse(input);
			}
			if (input.matches("..?/..?/....*$")) {
				final SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
				return ft.parse(input);
			}
			if (input.matches("..?/..?/..?$")) {
				final SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yy");
				return ft.parse(input);
			}
			if (input.matches("..?-..?-....")) {
				final SimpleDateFormat ft = new SimpleDateFormat("MM-dd-yyyy");
				return ft.parse(input);
			}
			if (input.matches("....-..?-..?")) {
				final SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
				return ft.parse(input);
			}
			if (input.matches("[0-9]{8}")) { // mmddyyyy
				final SimpleDateFormat ft = new SimpleDateFormat("MMddyyyy");
				return ft.parse(input);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertDate(final Date date, String outputFormat) {
		if (date == null || outputFormat == null) {
			return "";
		}
		String prefix = "";
		if (outputFormat.startsWith("cyy") ) {
			outputFormat = outputFormat.substring(1);
			final SimpleDateFormat out = new SimpleDateFormat("yyyy");
			prefix = String.valueOf(((Integer.parseInt(out.format(date).substring(1, 2)) + 1) % 10));
		}
		final SimpleDateFormat out = new SimpleDateFormat(outputFormat);
		return prefix + out.format(date);
	}

	public static String convertDate(String input, String inputFormat, String outputFormat) {
		if (input == null || (input = input.trim()).length() < 3 || input.matches("0*")) {
			return "";
		}
		String result = input;
		int century = -1;
		String prefix = "";
		try {
			if (input.length() < inputFormat.length()) {
				final int diff = inputFormat.length() - input.length();
				input = "000000000000".substring(0, diff) + input;
			}
			if (inputFormat.indexOf('c') > -1) {
				final int idx = inputFormat.indexOf('c');
				final char cent = input.charAt(idx);
				input = input.substring(0, idx) + input.substring(idx + 1);
				inputFormat = inputFormat.substring(0, idx) + inputFormat.substring(idx + 1);
				century = Integer.parseInt(String.valueOf(cent)) * 100;
			}
			final SimpleDateFormat in = new SimpleDateFormat(inputFormat);
			Date date = in.parse(input);
			if (century > -1) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				final int years = cal.get(Calendar.YEAR) % 100;
				cal.set(Calendar.YEAR, century + 1900 + years);
				date = cal.getTime();
				// date.setYear(century);
			}
			if (outputFormat.startsWith("c") || outputFormat.startsWith("c")) {
				outputFormat = outputFormat.substring(1);
				final SimpleDateFormat out = new SimpleDateFormat("yyyy");
				prefix = String.valueOf((Integer.parseInt(out.format(date).substring(1, 2)) + 1) % 10);
			}
			final SimpleDateFormat out = new SimpleDateFormat(outputFormat);
			result = prefix + out.format(date);
		} catch (final RuntimeException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date readDate(String input, String inputFormat) {
		if (input == null || input.length() < 3 || input.matches("0*")) {
			return null;
		}
		// int century =-1;
		if (input.length() < inputFormat.length()) {
			final int diff = inputFormat.length() - input.length();
			input = "000000000000".substring(0, diff) + input;
		}
		if (inputFormat.indexOf('c') > -1) {
			final int idx = inputFormat.indexOf('c');
			input = input.substring(0, idx) + input.substring(idx + 1);
			inputFormat = inputFormat.substring(0, idx) + inputFormat.substring(idx + 1);
		}

		final SimpleDateFormat in = new SimpleDateFormat(inputFormat);
		try {
			return in.parse(input);
		} catch (final ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public static long dateDiff(final Date date1, final Date date2, final String whatYouWant) {
		if (date1 == null || date2 == null) {
			return 0;
		}
		final Calendar calendar1 = Calendar.getInstance();
		final Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		return dateDiff(calendar1, calendar2, whatYouWant);
	}

	public static long dateDiff(final Calendar calendar1, final Calendar calendar2, final String whatYouWant) {
		try {
			// final int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

			long result = 0;
			final long milliseconds1 = calendar1.getTimeInMillis();
			final long milliseconds2 = calendar2.getTimeInMillis();
			final long diff = milliseconds1 - milliseconds2;

			if ("milliseconds".equalsIgnoreCase(whatYouWant.trim())) {
				result = diff;
			} else if ("seconds".equalsIgnoreCase(whatYouWant.trim())) {
				result = diff / 1000;
			} else if ("minutes".equalsIgnoreCase(whatYouWant.trim())) {
				result = diff / (60 * 1000);
			} else if ("hours".equalsIgnoreCase(whatYouWant.trim())) {
				result = diff / (60 * 60 * 1000);
			} else if ("days".equalsIgnoreCase(whatYouWant.trim())) {
				final Period p = new Period(calendar2.getTime().getTime(), calendar1.getTime().getTime(),
						PeriodType.days());
				return p.getDays();
			} else if ("years".equalsIgnoreCase(whatYouWant.trim())) {
				final Period p = new Period(calendar2.getTime().getTime(), calendar1.getTime().getTime(),
						PeriodType.years());
				return p.getYears();
			}
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static Date addDays(final Date date, final int days) {
		if (date == null) {
			return null;
		}
		final Calendar cthen = Calendar.getInstance();
		cthen.setTime(date);
		cthen.add(Calendar.DATE, days);
		return cthen.getTime();
	}
	
}