package com.server.dos.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.server.dos.Enum.OrderType.*;

@Getter
@RequiredArgsConstructor
public enum OrderState {
    START(1, WORKING), DESIGN(2, WORKING), PUBLISH(3, WORKING),
    IMPLEMENT(4, WORKING), FINAL(5, WORKING), COMPLETED(6, COMPLETE);
    private final int key;
    private final OrderType type;

    public static OrderState findWithKey(int key) {
        return Arrays.stream(values()).filter(state -> state.key == key).findAny().orElse(null);
    }
}
