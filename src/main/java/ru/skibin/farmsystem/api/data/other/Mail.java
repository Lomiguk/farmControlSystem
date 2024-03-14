package ru.skibin.farmsystem.api.data.other;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Mail {
    public final String targetEmail;
    public final String message;
}
