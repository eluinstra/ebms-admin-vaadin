package nl.clockwork.ebms.admin.components;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import io.vavr.Function1;

public interface WithDate
{
	static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	static final Function1<Instant,LocalDateTime> toLocalDateTime = time -> LocalDateTime.ofInstant(time,ZoneId.systemDefault());
}
