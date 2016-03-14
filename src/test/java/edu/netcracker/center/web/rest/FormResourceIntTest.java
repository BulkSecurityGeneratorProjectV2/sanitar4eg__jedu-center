package edu.netcracker.center.web.rest;

import edu.netcracker.center.Application;
import edu.netcracker.center.domain.Form;
import edu.netcracker.center.repository.FormRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FormResource REST controller.
 *
 * @see FormResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FormResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_PATH_TO_FILE = "AAAAA";
    private static final String UPDATED_PATH_TO_FILE = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    @Inject
    private FormRepository formRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFormMockMvc;

    private Form form;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormResource formResource = new FormResource();
        ReflectionTestUtils.setField(formResource, "formRepository", formRepository);
        this.restFormMockMvc = MockMvcBuilders.standaloneSetup(formResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        form = new Form();
        form.setPathToFile(DEFAULT_PATH_TO_FILE);
        form.setCreationTime(DEFAULT_CREATION_TIME);
    }

    @Test
    @Transactional
    public void createForm() throws Exception {
        int databaseSizeBeforeCreate = formRepository.findAll().size();

        // Create the Form

        restFormMockMvc.perform(post("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form)))
                .andExpect(status().isCreated());

        // Validate the Form in the database
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeCreate + 1);
        Form testForm = forms.get(forms.size() - 1);
        assertThat(testForm.getPathToFile()).isEqualTo(DEFAULT_PATH_TO_FILE);
        assertThat(testForm.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
    }

    @Test
    @Transactional
    public void checkPathToFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = formRepository.findAll().size();
        // set the field null
        form.setPathToFile(null);

        // Create the Form, which fails.

        restFormMockMvc.perform(post("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form)))
                .andExpect(status().isBadRequest());

        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = formRepository.findAll().size();
        // set the field null
        form.setCreationTime(null);

        // Create the Form, which fails.

        restFormMockMvc.perform(post("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form)))
                .andExpect(status().isBadRequest());

        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllForms() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the forms
        restFormMockMvc.perform(get("/api/forms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())))
                .andExpect(jsonPath("$.[*].pathToFile").value(hasItem(DEFAULT_PATH_TO_FILE.toString())))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)));
    }

    @Test
    @Transactional
    public void getForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get the form
        restFormMockMvc.perform(get("/api/forms/{id}", form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(form.getId().intValue()))
            .andExpect(jsonPath("$.pathToFile").value(DEFAULT_PATH_TO_FILE.toString()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingForm() throws Exception {
        // Get the form
        restFormMockMvc.perform(get("/api/forms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

		int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form
        form.setPathToFile(UPDATED_PATH_TO_FILE);
        form.setCreationTime(UPDATED_CREATION_TIME);

        restFormMockMvc.perform(put("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form)))
                .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeUpdate);
        Form testForm = forms.get(forms.size() - 1);
        assertThat(testForm.getPathToFile()).isEqualTo(UPDATED_PATH_TO_FILE);
        assertThat(testForm.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
    }

    @Test
    @Transactional
    public void deleteForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

		int databaseSizeBeforeDelete = formRepository.findAll().size();

        // Get the form
        restFormMockMvc.perform(delete("/api/forms/{id}", form.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeDelete - 1);
    }
}