package io.pivotal.pal.tracker;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    @Autowired
    private JdbcTemplate timeEntryTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.timeEntryTemplate= new JdbcTemplate(dataSource);

    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        timeEntryTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        timeEntry.setId(generatedKeyHolder.getKey().longValue());
        return timeEntry;

    }

    @Override
    public TimeEntry find(long id) {
        return timeEntryTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{id},
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return timeEntryTemplate.query("select id,project_id,user_id,date,hours from time_entries",mapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntryTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE time_entries SET  project_id=(?), user_id=(?), date=(?),hours=(?) where id=(?)"
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            statement.setLong(5, id);
            return statement;
        });
        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public void delete(long id) {
        timeEntryTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM time_entries where id=(?)"
            );

            statement.setLong(1, id);
            return statement;
        });
    }
    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
