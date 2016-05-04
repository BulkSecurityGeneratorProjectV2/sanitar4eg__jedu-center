package edu.netcracker.center.web.rest.util;

import com.codahale.metrics.annotation.Timed;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import edu.netcracker.center.domain.QStudent;
import edu.netcracker.center.domain.Student;
import edu.netcracker.center.domain.util.OperationResult;
import edu.netcracker.center.service.HistoryService;
import edu.netcracker.center.service.ImportService;
import edu.netcracker.center.service.StudentService;
import edu.netcracker.center.service.StudentXslView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * REST controller for managing integrations of Student.
 */
@RestController
@RequestMapping("/api")
public class StudentIntegrationResource {

    private final Logger log = LoggerFactory.getLogger(StudentIntegrationResource.class);

    @Inject
    private StudentService studentService;

    @Inject
    private HistoryService historyService;

    @Inject
    private ImportService importService;

    /**
     * GET  /history/students/:dateTime -> get history of students by "dateTime".
     */
    @RequestMapping(value = "/history/students/{dateTime:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Student> getHistoryOfStudents(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateTime) {
        log.debug("REST request to get history of Students by date : {}", dateTime);
        return historyService.getHistoryOfStudents(dateTime);
    }

    /**
     * GET  /export/students -> get XSL file with all students.
     */
    @RequestMapping(value = "/export/students",
        method = RequestMethod.GET)
    @Timed
    public ModelAndView getXslOfStudents() {
        log.debug("REST request to get XSL of Students");
        Predicate predicate = QStudent.student.isActive.eq(true);
        return new ModelAndView(new StudentXslView(), "students", studentService.findAll(predicate));
    }

    /**
     * POST  /import/students -> upload file with students for saving.
     */
    @RequestMapping(value = "/import/students",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public @ResponseBody Collection<OperationResult> handleImport(@RequestParam("file") MultipartFile file) {
        log.debug("REST request to upload XSL of Students");
        return importService.handleImportOfStudents(file);
    }

}
