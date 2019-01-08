package io.pivotal.pal.tracker;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> map = new HashMap<Long, TimeEntry>();
    private AtomicLong timeEntrySequence = new AtomicLong(1L);

    public TimeEntry create(TimeEntry timeEntry) {
        long newId = this.timeEntrySequence.getAndIncrement();
        System.out.println("newId= " + newId);
        timeEntry.setId(newId);
        map.put(newId, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return map.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(map.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        map.replace(id, timeEntry);

        return timeEntry;
    }

    public void delete(long id) {
            map.remove(id);
    }
}
