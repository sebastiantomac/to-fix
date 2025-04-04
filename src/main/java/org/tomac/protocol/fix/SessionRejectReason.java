package org.tomac.protocol.fix;

public enum SessionRejectReason {
    INVALID_TAG_NUMBER(0),
    REQUIRED_TAG_MISSING(1),
    TAG_SPECIFIED_WITHOUT_A_VALUE(2),
    VALUE_IS_INCORRECT_OUT_OF_RANGE_FOR_THIS_TAG(3),
    INCORRECT_DATA_FORMAT_FOR_VALUE(4),
    DECRYPTION_PROBLEM(5),
    SIGNATURE_PROBLEM(6),
    COMPID_PROBLEM(7),
    SENDINGTIME_ACCURACY_PROBLEM(8),
    INVALID_MSGTYPE(9),
    XML_VALIDATION_ERROR(10),
    TAG_APPEARS_MORE_THAN_ONCE(11),
    TAG_SPECIFIED_OUT_OF_REQUIRED_ORDER(12),
    REPEATING_GROUP_FIELDS_OUT_OF_ORDER(13),
    INCORRECT_NUMINGROUP_COUNT_FOR_REPEATING_GROUP(14),
    NON_DATA_VALUE_INCLUDES_FIELD_DELIMITER(15),
    OTHER(16);

    private final int value;

    SessionRejectReason(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SessionRejectReason fromValue(long value) {
        for (SessionRejectReason reason : SessionRejectReason.values()) {
            if (reason.value == value) {
                return reason;
            }
        }
        return OTHER;
    }
}
