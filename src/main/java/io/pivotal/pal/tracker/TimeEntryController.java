package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @ResponseBody
    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
//        URI newResource = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(newResource);
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);

        // return ResponseEntity.created(newResource).body(createdTimeEntry);
        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("//time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry readEntry = timeEntryRepository.find(id);
        if (readEntry != null)
            return ResponseEntity.ok(readEntry);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry updatedTimeEntry) {
        TimeEntry fromRepo = timeEntryRepository.update(id, updatedTimeEntry);
        boolean entryExists = fromRepo != null;

        if (entryExists)
            return ResponseEntity.ok(fromRepo);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
