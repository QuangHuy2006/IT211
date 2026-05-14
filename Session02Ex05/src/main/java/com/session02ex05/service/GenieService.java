package com.session02ex05.service;

import com.session02ex05.dto.WishResponse;
import com.session02ex05.exception.BadWishException;
import com.session02ex05.exception.WishLimitExceededException;
import com.session02ex05.model.WishHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenieService {

    private static final int MAX_WISHES = 3;

    private int usedWishes = 0;

    private final List<WishHistory> histories = new ArrayList<>();

    public WishResponse wishForJob(String content) {
        validateWishLimit();
        validateForbiddenWish(content);

        usedWishes++;

        String message = "Thần đèn đã giúp bạn tìm được một công việc tốt hơn!";
        histories.add(new WishHistory("FIND_JOB", content, "ACCEPTED", message));

        return new WishResponse(message, getRemainingWishes());
    }

    public WishResponse wishForSkill(String content) {
        validateWishLimit();
        validateForbiddenWish(content);

        usedWishes++;

        String message = "Thần đèn đã ban cho bạn kỹ năng học lập trình nhanh hơn!";
        histories.add(new WishHistory("IMPROVE_SKILL", content, "ACCEPTED", message));

        return new WishResponse(message, getRemainingWishes());
    }

    public WishResponse wishForAdvice(String content) {
        validateWishLimit();
        validateForbiddenWish(content);

        usedWishes++;

        String message = "Lời khuyên của thần đèn: Hãy học đều mỗi ngày và thực hành thật nhiều!";
        histories.add(new WishHistory("GET_ADVICE", content, "ACCEPTED", message));

        return new WishResponse(message, getRemainingWishes());
    }

    public List<WishHistory> getHistories() {
        return histories;
    }

    private void validateWishLimit() {
        if (usedWishes >= MAX_WISHES) {
            histories.add(new WishHistory(
                    "UNKNOWN",
                    "Yêu cầu vượt quá số lượt",
                    "REJECTED",
                    "Thần đèn đã hết lượt ước!"
            ));

            throw new WishLimitExceededException("Thần đèn đã hết lượt ước!");
        }
    }

    private void validateForbiddenWish(String content) {
        String lowerContent = content.toLowerCase();

        if (lowerContent.contains("thêm điều ước")
                || lowerContent.contains("giàu")
                || lowerContent.contains("vô hạn")) {

            String message = "Điều ước không hợp lệ! Thần đèn không thể cho thêm điều ước hoặc làm bạn giàu ngay lập tức.";

            histories.add(new WishHistory(
                    "FORBIDDEN_WISH",
                    content,
                    "REJECTED",
                    message
            ));

            throw new BadWishException(message);
        }
    }

    private int getRemainingWishes() {
        return MAX_WISHES - usedWishes;
    }
}
