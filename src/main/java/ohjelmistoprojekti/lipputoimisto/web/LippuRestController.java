package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.dto.LippuLunastus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.LippuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.dto.LippuVastausDto;

@RestController
@RequestMapping("/api/liput")
public class LippuRestController {

    private final LippuService lippuService;

    public LippuRestController(
            LipputyyppiRepository lipputyyppiRepository,
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            TapahtumaRepository tapahtumaRepository, LippuService lippuService) {

        this.lippuService = lippuService;
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping("/koodi/{koodi}")
    public ResponseEntity<LippuVastausDto> haeLippuKoodilla(@PathVariable String koodi) {
        return lippuService.haeLippuTiedoilla(koodi);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/varaa")
    public ResponseEntity<?> varaaLiput(@Valid @RequestBody LippuVarausPyynto pyynto) {
        return lippuService.varaaLiput(pyynto);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/lunasta")
    public ResponseEntity<?> lunastaLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.lunastaLippu(pyynto);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/peru")
    public ResponseEntity<?> peruLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.peruLippu(pyynto);
    }
}