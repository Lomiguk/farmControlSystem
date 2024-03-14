package ru.skibin.farmsystem.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.response.MarkResponse;
import ru.skibin.farmsystem.entity.MarkEntity;

import java.util.Collection;
import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class MarkCollectionMapper {
    private final EntityToResponseMapper entityMapper;

    public Collection<MarkResponse> mapEntitiesToResponses(Collection<MarkEntity> entities) {
        Collection<MarkResponse> result = new LinkedList<>();
        for (var entity : entities) {
            result.add(entityMapper.toResponse(entity));
        }

        return result;
    }
}