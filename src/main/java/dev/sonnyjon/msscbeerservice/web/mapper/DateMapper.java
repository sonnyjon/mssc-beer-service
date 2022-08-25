package dev.sonnyjon.msscbeerservice.web.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Created by Sonny on 8/25/2022.
 */
@Component
public class DateMapper
{
    public OffsetDateTime asOffsetDateTime(Timestamp ts)
    {
        if (ts != null)
        {
            return OffsetDateTime.of(
                    ts.toLocalDateTime().getYear(),
                    ts.toLocalDateTime().getMonthValue(),
                    ts.toLocalDateTime().getDayOfMonth(),
                    ts.toLocalDateTime().getHour(),
                    ts.toLocalDateTime().getMinute(),
                    ts.toLocalDateTime().getSecond(),
                    ts.toLocalDateTime().getNano(),
                    ZoneOffset.UTC
            );
        }
        else return null;
    }

    public Timestamp asTimestamp(OffsetDateTime offsetDateTime)
    {
        return (offsetDateTime != null)
                ? Timestamp.valueOf(offsetDateTime.atZoneSameInstant( ZoneOffset.UTC ).toLocalDateTime())
                : null;
    }
}