package ru.practicum.ewm.compilation.srvice;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilation);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest upd);

    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto findById(Long compId);
}
