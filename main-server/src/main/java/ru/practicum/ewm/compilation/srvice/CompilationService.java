package ru.practicum.ewm.compilation.srvice;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilation);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest upd);
}
