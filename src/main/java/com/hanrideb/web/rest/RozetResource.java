package com.hanrideb.web.rest;

import com.hanrideb.domain.Rozet;
import com.hanrideb.repository.RozetRepository;
import com.hanrideb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hanrideb.domain.Rozet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RozetResource {

    private final Logger log = LoggerFactory.getLogger(RozetResource.class);

    private static final String ENTITY_NAME = "rozet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RozetRepository rozetRepository;

    public RozetResource(RozetRepository rozetRepository) {
        this.rozetRepository = rozetRepository;
    }

    /**
     * {@code POST  /rozets} : Create a new rozet.
     *
     * @param rozet the rozet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rozet, or with status {@code 400 (Bad Request)} if the rozet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rozets")
    public ResponseEntity<Rozet> createRozet(@Valid @RequestBody Rozet rozet) throws URISyntaxException {
        log.debug("REST request to save Rozet : {}", rozet);
        if (rozet.getId() != null) {
            throw new BadRequestAlertException("A new rozet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rozet result = rozetRepository.save(rozet);
        return ResponseEntity
            .created(new URI("/api/rozets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rozets/:id} : Updates an existing rozet.
     *
     * @param id the id of the rozet to save.
     * @param rozet the rozet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rozet,
     * or with status {@code 400 (Bad Request)} if the rozet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rozet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rozets/{id}")
    public ResponseEntity<Rozet> updateRozet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rozet rozet)
        throws URISyntaxException {
        log.debug("REST request to update Rozet : {}, {}", id, rozet);
        if (rozet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rozet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rozetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rozet result = rozetRepository.save(rozet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rozet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rozets/:id} : Partial updates given fields of an existing rozet, field will ignore if it is null
     *
     * @param id the id of the rozet to save.
     * @param rozet the rozet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rozet,
     * or with status {@code 400 (Bad Request)} if the rozet is not valid,
     * or with status {@code 404 (Not Found)} if the rozet is not found,
     * or with status {@code 500 (Internal Server Error)} if the rozet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rozets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rozet> partialUpdateRozet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rozet rozet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rozet partially : {}, {}", id, rozet);
        if (rozet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rozet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rozetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rozet> result = rozetRepository
            .findById(rozet.getId())
            .map(existingRozet -> {
                if (rozet.getRozetIsmi() != null) {
                    existingRozet.setRozetIsmi(rozet.getRozetIsmi());
                }
                if (rozet.getRozetResim() != null) {
                    existingRozet.setRozetResim(rozet.getRozetResim());
                }
                if (rozet.getRozetResimContentType() != null) {
                    existingRozet.setRozetResimContentType(rozet.getRozetResimContentType());
                }

                return existingRozet;
            })
            .map(rozetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rozet.getId().toString())
        );
    }

    /**
     * {@code GET  /rozets} : get all the rozets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rozets in body.
     */
    @GetMapping("/rozets")
    public List<Rozet> getAllRozets() {
        log.debug("REST request to get all Rozets");
        return rozetRepository.findAll();
    }

    /**
     * {@code GET  /rozets/:id} : get the "id" rozet.
     *
     * @param id the id of the rozet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rozet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rozets/{id}")
    public ResponseEntity<Rozet> getRozet(@PathVariable Long id) {
        log.debug("REST request to get Rozet : {}", id);
        Optional<Rozet> rozet = rozetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rozet);
    }

    /**
     * {@code DELETE  /rozets/:id} : delete the "id" rozet.
     *
     * @param id the id of the rozet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rozets/{id}")
    public ResponseEntity<Void> deleteRozet(@PathVariable Long id) {
        log.debug("REST request to delete Rozet : {}", id);
        rozetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
