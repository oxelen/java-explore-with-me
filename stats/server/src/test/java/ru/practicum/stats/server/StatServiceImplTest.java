package ru.practicum.stats.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.CreateHitDto;
import ru.practicum.stats.dto.ResponseHitDto;
import ru.practicum.stats.server.stat.model.Hit;
import ru.practicum.stats.server.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatServiceImplTest {
    private final EntityManager em;

    private final StatService statService;

    @Test
    public void createHitTest() {
        CreateHitDto createDto = CreateHitDto.builder()
                .app("testApp")
                .uri("testUri")
                .ip("testIp")
                .build();

        Hit found = saveHit(createDto);

        assertThat(found.getId(), notNullValue());
        assertThat(found.getApp(), equalTo(createDto.getApp()));
        assertThat(found.getUri(), equalTo(createDto.getUri()));
        assertThat(found.getIp(), equalTo(createDto.getIp()));
    }

    @Test
    public void findHitsTest() {
        CreateHitDto createDto = CreateHitDto.builder()
                .app("testApp")
                .uri("testUri")
                .ip("testIp")
                .build();

        Hit found = saveHit(createDto);
        LocalDateTime start = found.getTimestamp().minusMinutes(1);
        LocalDateTime end = found.getTimestamp().plusMinutes(1);
        String uri = found.getUri();

        List<ResponseHitDto> res = statService.findHits(start, end, new String[]{uri}, true);

        assertThat(res, notNullValue());
        assertThat(res.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListByAllUrisWhenUrisParameterIsNull() {
        CreateHitDto firstHitDto = CreateHitDto.builder()
                .app("firstApp")
                .uri("firstApp")
                .ip("firstApp")
                .build();

        CreateHitDto secondHitDto = CreateHitDto.builder()
                .app("secondApp")
                .uri("secondApp")
                .ip("secondApp")
                .build();
        Hit firstFound = saveHit(firstHitDto);
        Hit secondFound = saveHit(secondHitDto);

        LocalDateTime start = firstFound.getTimestamp().minusMinutes(1);
        LocalDateTime end = secondFound.getTimestamp().plusMinutes(1);

        List<ResponseHitDto> res = statService.findHits(start, end, null, true);

        assertThat(res, notNullValue());
        assertThat(res.size(), equalTo(2));

        List<String> foundUris = res.stream().map(ResponseHitDto::getUri).toList();
        assertThat(firstFound.getUri(), in(foundUris));
        assertThat(secondFound.getUri(), in(foundUris));
    }

    @Test
    public void shouldReturnUniqueHitsWhenParameterUniqueIsTrue() {
        final String sameApp = "sameApp";
        final String sameIp = "sameIp";
        final String sameUri = "sameUri";

        CreateHitDto firstHitDto = CreateHitDto.builder()
                .app(sameApp)
                .uri(sameUri)
                .ip(sameIp)
                .build();

        CreateHitDto secondHitDto = CreateHitDto.builder()
                .app(sameApp)
                .uri(sameUri)
                .ip(sameIp)
                .build();

        statService.hit(firstHitDto);
        statService.hit(secondHitDto);

        LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        List<ResponseHitDto> res = statService.findHits(start, end, null, true);

        assertThat(res, notNullValue());
        assertThat(res.size(), equalTo(1));
        assertThat(res.getFirst().getHits(), equalTo(1L));
    }

    @Test
    public void shouldReturnNotUniqueHitsWhenParameterUniqueIsFalse() {
        final String sameApp = "sameApp";
        final String sameIp = "sameIp";
        final String sameUri = "sameUri";

        CreateHitDto firstHitDto = CreateHitDto.builder()
                .app(sameApp)
                .uri(sameUri)
                .ip(sameIp)
                .build();

        CreateHitDto secondHitDto = CreateHitDto.builder()
                .app(sameApp)
                .uri(sameUri)
                .ip(sameIp)
                .build();

        statService.hit(firstHitDto);
        statService.hit(secondHitDto);

        LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        List<ResponseHitDto> res = statService.findHits(start, end, null, false);

        assertThat(res, notNullValue());
        assertThat(res.size(), equalTo(1));
        assertThat(res.getFirst().getHits(), equalTo(2L));
    }

    private Hit saveHit(CreateHitDto createHitDto) {
        statService.hit(createHitDto);
        TypedQuery<Hit> query = em.createQuery("select h from Hit h where h.app = :app", Hit.class);
        return query.setParameter("app", createHitDto.getApp()).getSingleResult();
    }
}
