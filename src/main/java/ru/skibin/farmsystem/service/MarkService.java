package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.request.mark.AddMarkRequest;
import ru.skibin.farmsystem.api.request.mark.UpdateMarkRequest;
import ru.skibin.farmsystem.api.response.MarkResponse;
import ru.skibin.farmsystem.entity.MarkEntity;
import ru.skibin.farmsystem.repository.MarkDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
import ru.skibin.farmsystem.service.mapper.MarkCollectionMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;
import ru.skibin.farmsystem.util.LimitOffsetTransformer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MarkService {
    private static final Logger LOGGER = Logger.getLogger(MarkService.class.getName());
    private final MarkDAO markDAO;
    private final CommonCheckHelper checkHelper;
    private final EntityToResponseMapper entityMapper;
    private final MarkCollectionMapper markCollectionMapper;
    private final LimitOffsetTransformer limitOffsetTransformer;

    /**
     * Add mark to repository
     *
     * @param bindingResult  Request validation result
     * @param addMarkRequest request with mark data
     * @return mark response model
     */
    @Transactional
    public MarkResponse addMark(BindingResult bindingResult, AddMarkRequest addMarkRequest, Long profileId) {
        checkHelper
                .chainCheckAuthPermission(profileId)
                .chainCheckValidation(bindingResult);

        long id = markDAO.save(
                profileId,
                addMarkRequest.getMark(),
                addMarkRequest.getDate() == null ? LocalDate.now() : addMarkRequest.getDate()
        );
        LOGGER.info(String.format("Mark %d  was added to repository", id));
        return getMark(id);
    }

    /**
     * Get mark from repository
     *
     * @param id mark numerical identifier
     * @return Mark response model
     */
    public MarkResponse getMark(Long id) {
        checkHelper.chainCheckAuthPermission(id);
        MarkEntity mark = checkHelper.checkMarkForExist(id);
        LOGGER.info(String.format("Mark %d was got", id));
        return entityMapper.toResponse(mark);
    }

    /**
     * Find mark by profile id
     *
     * @param profileId Profile numerical identifier
     * @param limit     Pagination's limit
     * @param offset    Pagination's offset
     * @return Collection of mar response models
     */
    @Transactional
    public Collection<MarkResponse> findMarksByProfile(Long profileId, Integer limit, Integer offset) {
        checkHelper.checkProfileForExist(profileId);

        limit = limitOffsetTransformer.getLimit(limit);
        offset = limitOffsetTransformer.getOffset(offset);
        LOGGER.info(String.format("Try to get marks by profile %d", profileId));
        return markCollectionMapper.mapEntitiesToResponses(
                markDAO.findByProfileId(profileId, limit, offset)
        );
    }

    /**
     * Find marks by date
     *
     * @param day    Date
     * @param limit  Pagination limit
     * @param offset Pagination offset
     * @return Collection of mar response models
     */
    public Collection<MarkResponse> findMarksByDay(LocalDate day, Integer limit, Integer offset) {
        limit = limitOffsetTransformer.getLimit(limit);
        offset = limitOffsetTransformer.getOffset(offset);
        LOGGER.info(String.format("Try to get marks by day - %s", day));
        return markCollectionMapper.mapEntitiesToResponses(
                markDAO.findAllByDay(day, limit, offset)
        );
    }

    /**
     * Find marks by period
     *
     * @param periodRequest Request with period data
     * @param limit         Pagination's limit
     * @param offset        Pagination's offset
     * @return Collection of mar response models
     */
    @Transactional
    public Collection<MarkResponse> findMarksByPeriod(
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        checkHelper.chainCheckStartEndOfPeriod(periodRequest.getStart(), periodRequest.getEnd());
        limit = limitOffsetTransformer.getLimit(limit);
        offset = limitOffsetTransformer.getOffset(offset);
        LOGGER.info(String.format("Try to get marks by period %s - %s", periodRequest.getStart(), periodRequest.getEnd()));
        return markCollectionMapper.mapEntitiesToResponses(
                markDAO.findAllByPeriod(periodRequest.getStart(), periodRequest.getEnd(), limit, offset)
        );
    }

    /**
     * Update mark data
     *
     * @param bindingResult     Request validation data
     * @param id                Mark numerical identifier
     * @param updateMarkRequest Request with new mark data
     * @return Mark response model
     */
    @Transactional
    public MarkResponse updateMark(BindingResult bindingResult, Long id, UpdateMarkRequest updateMarkRequest) {
        checkHelper.chainCheckValidation(bindingResult)
                .checkMarkForExist(id);
        markDAO.update(
                id,
                updateMarkRequest.getProfileId(),
                updateMarkRequest.getMark(),
                updateMarkRequest.getDate()
        );
        LOGGER.info(String.format("Mark %d was updated", id));
        return getMark(id);
    }

    /**
     * Deleting mark from repository
     *
     * @param id  mark numerical identifier
     * @return true - if mark is deleted
     */
    public Boolean deleteMark(Long id) {
        LOGGER.info(String.format("Try to delete mark %d", id));
        return markDAO.delete(id) > 0;
    }
}
