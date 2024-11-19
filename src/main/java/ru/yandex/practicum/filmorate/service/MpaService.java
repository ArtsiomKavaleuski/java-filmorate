package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    @Autowired
    private final MpaStorage mpaStorage;

    public Collection<MPA> getAll() {
        return mpaStorage.getAll();
    }

    public MPA getMpaById(long id) {
        if (mpaStorage.getMpaById(id) == null) {
            log.warn("Рейтинг с id = {} не найден.", id);
            throw new NotFoundException("Рейтинг с id = " + id + " не найден");
        }
        return mpaStorage.getMpaById(id);
    }
}