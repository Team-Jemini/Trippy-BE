package org.scoula.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateFormatUtil {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yy.MM.dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public static String formatDateTime(LocalDate date, LocalDateTime time) {
		String datePart = DATE_FORMATTER.format(date);
		String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
		String timePart = TIME_FORMATTER.format(time);
		return String.format("%s(%s) %s", datePart, dayOfWeek, timePart);
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		String datePart = DATE_FORMATTER.format(dateTime.toLocalDate());
		String dayOfWeek = dateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
		String timePart = TIME_FORMATTER.format(dateTime);
		return String.format("%s(%s) %s", datePart, dayOfWeek, timePart);
	}
}