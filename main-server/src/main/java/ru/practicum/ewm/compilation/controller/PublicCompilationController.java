package ru.practicum.ewm.compilation.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.srvice.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> findAll(@RequestParam(required = false) Boolean pinned,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("GET all compilations");
        List<CompilationDto> res = compilationService.findAll(pinned, from, size);
        log.info("compilations found");

        return ResponseEntity.status(200).body(res);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> findById(@PositiveOrZero @PathVariable Long compId) {
        log.info("GET compilation by id {}", compId);
        CompilationDto res = compilationService.findById(compId);
        log.info("compilation found id {}", compId);

        return ResponseEntity.status(200).body(res);
    }
}
