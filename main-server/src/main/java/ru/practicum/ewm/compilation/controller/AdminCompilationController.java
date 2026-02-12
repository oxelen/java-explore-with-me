package ru.practicum.ewm.compilation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.srvice.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> create(@Valid @RequestBody NewCompilationDto dto) {
        log.info("POST new compilation");
        CompilationDto res = compilationService.create(dto);
        log.info("compilation created");

        return ResponseEntity.status(201).body(res);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PositiveOrZero @PathVariable Long compId) {
        log.info("DELETE compilation, id={}", compId);
        compilationService.delete(compId);
        log.info("compilation id={} delete", compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PositiveOrZero @PathVariable Long compId,
                                                 @Valid @RequestBody UpdateCompilationRequest dto) {
        log.info("PATCH compilation, id={}", compId);
        CompilationDto res = compilationService.update(compId, dto);
        log.info("compilation updated, id={}", compId);

        return ResponseEntity.status(200).body(res);
    }
}
